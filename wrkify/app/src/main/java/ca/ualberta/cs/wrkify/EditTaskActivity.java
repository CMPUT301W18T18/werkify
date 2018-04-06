/*
 * Copyright 2018 CMPUT301W18T18
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package ca.ualberta.cs.wrkify;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Activity for a task requester to edit a task that they own
 * or to create a new task. An existing task should be passed
 * in as EXTRA_EXISTING_TASK. If it is unset, a new task will
 * be created. Edited/created tasks will be returned as
 * EXTRA_RETURNED_TASK, with an appropriate result code:
 *     RESULT_TASK_CREATED for a new task
 *     RESULT_OK for an edited task
 * If the task is deleted from this activity,
 * EXTRA_RETURNED_TASK will be unset and the result will be
 * RESULT_TASK_DELETED. The parent activity must perform the
 * actual deletion of the task in this case.
 */
public class EditTaskActivity extends AppCompatActivity {

    public class ImageManager{
        private ArrayList<RemoteReference<CompressedBitmap>> remoteImages;
        private ArrayList<CompressedBitmap> localImages;
        private ArrayList<CompressedBitmap> thumbnails;
        private ArrayList<RemoteReference<CompressedBitmap>> toDelete;


        public ImageManager() {
            remoteImages = new ArrayList<>();
            localImages = new ArrayList<>();
            thumbnails = new ArrayList<>();
            toDelete = new ArrayList<>();
        }

        public void setRemoteImages(ArrayList<RemoteReference<CompressedBitmap>> list) {
            this.remoteImages = list;
        }

        public void setThumbnails(ArrayList<CompressedBitmap> thumbnails) {
            this.thumbnails = thumbnails;
        }

        public void addImagePair(CompressedBitmap thumbnail, CompressedBitmap image) {
            localImages.add(image);
            thumbnails.add(thumbnail);
        }

        public void deleteImageFromThumbnail(CompressedBitmap thumbnail) {
            int index = thumbnails.indexOf(thumbnail);
            if (index >= remoteImages.size()) {
                localImages.remove(index - remoteImages.size());
                thumbnails.remove(index);
            } else {
                toDelete.add(remoteImages.get(index));
                toDelete.add(thumbnail.<CompressedBitmap>reference());
                thumbnails.remove(index);
                remoteImages.remove(index);
            }
        }

        public CompressedBitmap getImage(int index) {
            if (index >= remoteImages.size()) {
                return localImages.get(index - remoteImages.size());
            } else {
                try {
                    return remoteImages.get(index).getRemote(WrkifyClient.getInstance(), CompressedBitmap.class);
                } catch (IOException e) {
                    Log.e("Didn't work", "couldn't get image from image list in activity");
                    return null;
                }
            }

        }

        private void flushDeletions() {
            for (int i = 0; i < toDelete.size(); i++) {
                try {
                    WrkifyClient.getInstance().delete(toDelete.get(i).getRemote(WrkifyClient.getInstance(), CompressedBitmap.class));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        private void uploadLocalImages(Task task) {
            int thumbnailIndexOffset = remoteImages.size();
            for (int i = 0; i < localImages.size(); i++) {
                CompressedBitmap uploadedImage = WrkifyClient.getInstance().create(CompressedBitmap.class, localImages.get(i).getData());
                remoteImages.add(uploadedImage.<CompressedBitmap>reference());

                int thumbnailIndex = i + thumbnailIndexOffset;
                CompressedBitmap uploadedThumbnail = WrkifyClient.getInstance().create(CompressedBitmap.class, thumbnails.get(thumbnailIndex).getData());
                thumbnails.set(thumbnailIndex, uploadedThumbnail);
            }
            localImages.clear();
            ArrayList<RemoteReference<CompressedBitmap>> newRemoteThumbnails = new ArrayList<>();
            for (int i = 0; i < thumbnails.size(); i++) {
                newRemoteThumbnails.add(thumbnails.get(i).<CompressedBitmap>reference());
            }

            task.setRemoteImages(remoteImages);
            task.setRemoteThumbnails(newRemoteThumbnails);

        }

        public void save(Task task) {
            flushDeletions();
            uploadLocalImages(task);

            //Delete, upload, upload task
        }
    }

    /** Task being passed in to EditTaskActivity */
    public static final String EXTRA_EXISTING_TASK = "ca.ualberta.cs.wrkify.EXTRA_EXISTING_TASK";

    /** Task being passed back from EditTaskActivity */
    public static final String EXTRA_RETURNED_TASK = "ca.ualberta.cs.wrkify.EXTRA_RETURNED_TASK";

    /** The task being returned is a newly-created task and should be added to the appropriate context */
    public static final int RESULT_TASK_CREATED = 11;

    /** The task being edited was deleted and should be removed from its context */
    public static final int RESULT_TASK_DELETED = 12;


    private Task task;
    private CheckList checkList;
    private boolean taskIsNew = false;

    private EditText titleField;
    private EditText descriptionField;
    private CheckListEditorView checkListEditorView;
    private Button checkListNewButton;
    private Button checkListAddButton;

    private RecyclerView recyclerView;
    private TaskImageListAdapter adapter;
    private ImageManager imageManager;

    private static final int REQUEST_IMAGE_CAMERA = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        ActionBar actionBar = getSupportActionBar();

        imageManager = new ImageManager();

        this.titleField = findViewById(R.id.editTaskTitleField);
        this.descriptionField = findViewById(R.id.editTaskDescriptionField);
        this.checkListEditorView = findViewById(R.id.editTaskChecklist);
        this.checkListNewButton = findViewById(R.id.editTaskButtonChecklistNew);
        this.checkListAddButton = findViewById(R.id.editTaskButtonChecklistAdd);

        this.task = (Task) getIntent().getSerializableExtra(EXTRA_EXISTING_TASK);

        if (this.task == null) {
            // TODO this may or may not need to be changed to allow server connectivity
            // TODO change the new user thing later

            this.taskIsNew = true;
            this.checkList = new CheckList();
            if (actionBar != null) {
                actionBar.setTitle("New task");
            }
        } else {
            this.taskIsNew = false;
            this.checkList = task.getCheckList();
            if (actionBar != null) {
                actionBar.setTitle("Editing task");
                actionBar.setSubtitle(task.getTitle());
            }

            // populate fields
            titleField.setText(task.getTitle());
            descriptionField.setText(task.getDescription());

            if (task.getCheckList().itemCount() > 0) {
                showChecklistEditor();
            }
        }

        checkListNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkList.addItem("");
                checkListEditorView.notifyDataSetChanged();
                showChecklistEditor();
            }
        });

        checkListAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkList.addItem("");
                checkListEditorView.notifyDataSetChanged();
            }
        });

        checkListEditorView.setCheckList(checkList);
        checkListEditorView.setOnDataSetChangedListener(new CheckListView.OnDataSetChangedListener() {
            @Override
            public void onDataSetChanged(@Nullable CheckList data) {
                if (data == null || data.itemCount() == 0) {
                    hideChecklistEditor();
                }
            }
        });

        if (task != null) {
            try {

                //task.setRemoteThumbnails(new ArrayList<RemoteReference<CompressedBitmap>>());
                //task.setRemoteImages(new ArrayList<RemoteReference<CompressedBitmap>>());

                ArrayList<CompressedBitmap> thumbnails = task.getCompressedThumbnails();
                ArrayList<RemoteReference<CompressedBitmap>> remoteImages = task.getRemoteImages();

                imageManager.setThumbnails(thumbnails);
                imageManager.setRemoteImages(remoteImages);

                adapter = new TaskImageListAdapter(thumbnails) {
                    @Override
                    public void buttonClicked(int position) {
                        showImage(position);
                    }
                };

                recyclerView = findViewById(R.id.editTaskImageRecyclerView);
                LinearLayoutManager manager = new LinearLayoutManager(this);
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } catch (IOException e) {
                Log.e("EditTaskActivity", e.toString());
            }


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAMERA && resultCode == RESULT_OK) {
            try {
                Uri uri = Uri.fromFile(new File(currentImagePath));
                Bitmap image = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                if (image != null) {
                    // https://stackoverflow.com/questions/8560501/android-save-image-into-gallery https://stackoverflow.com/a/8722494 (line below)
                    MediaStore.Images.Media.insertImage(getContentResolver(), image, "Wrkify image", "Wrkify image");

                    //Add to ImageManager, NOT Task
                    CompressedBitmap compressedImage = ImageUtilities.makeCompressedImage(image);
                    CompressedBitmap compressedThumbnail = ImageUtilities.makeCompressedThumbnail(image);

                    imageManager.addImagePair(compressedThumbnail, compressedImage);
                    adapter.notifyDataSetChanged();
                }
            } catch (IOException e) {
                Log.e("EditTaskActivity", e.toString());
            }
            //Add the image
        } else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                CompressedBitmap compressedImage = ImageUtilities.makeCompressedImage(bm);
                CompressedBitmap compressedThumbnail = ImageUtilities.makeCompressedThumbnail(bm);
                imageManager.addImagePair(compressedThumbnail, compressedImage);
                adapter.notifyDataSetChanged();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void addPhotoClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.get_image_dialog_title).setItems(R.array.get_image_dialog_choices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        openCamera();
                        break;

                    case 1:
                        openGallery();
                        break;
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    //Taken from https://developer.android.com/training/camera/photobasics.html
    protected String currentImagePath;
    protected File makeNewImage() throws IOException {
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = "wrkify_" + time;
        String extension = ".jpg";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(filename, extension, storageDir);
        currentImagePath = imageFile.getAbsolutePath();
        return imageFile;
    }

    protected void openCamera() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (i.resolveActivity(getPackageManager()) != null) {
            try {
                File imageFile = makeNewImage();
                Uri uri = FileProvider.getUriForFile(this, "ca.ualberta.cs.wrkify.fileprovider", imageFile);
                i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(i, REQUEST_IMAGE_CAMERA);
            } catch (IOException e) {
                Log.e("EditTaskActivity", e.toString());
            }

        }
    }

    protected void openGallery() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        if (i.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(i, REQUEST_IMAGE_GALLERY);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_task, menu);

        MenuItem saveItem = menu.findItem(R.id.menuItemSaveTask);
        MenuItem deleteItem = menu.findItem(R.id.menuItemDeleteTask);
        MenuItem addPhoto = menu.findItem(R.id.menuItemAddPhoto);

        addPhoto.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                addPhotoClicked();
                return true;
            }
        });

        if (this.taskIsNew) {
            // Set up menu for new task:

            // Save button is labelled "post"
            saveItem.setTitle("Post");

            // Delete button is not available
            deleteItem.setVisible(false);
        }
        else {
            // Set up menu for editing existing task:

            // Delete button deletes task
            deleteItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    ConfirmationDialogFragment confirmation = ConfirmationDialogFragment.makeDialog(
                            "Delete this task?",
                            "Cancel", "Delete",
                            new ConfirmationDialogFragment.OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    deleteAndFinish();
                                }
                            }
                    );
                    confirmation.show(getFragmentManager(), null);
                    return true;
                }
            });
        }

        // Clicking save always saves task
        saveItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    saveAndFinish();
                    return true;
                }
            });

        return true;
    }

    public void showImage(int position) {
        //COME BACK HERE
        Bitmap bm = null;
        CompressedBitmap cb = imageManager.getImage(position);
        if (cb == null) {
            return;
        }
        bm = cb.getBitmap();

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setType("image/*");

        try {
            File dir = new File(getCacheDir(), "dataHere");
            dir.mkdir();
            File f = new File(dir, "sending.png");

            FileOutputStream fos = new FileOutputStream(f);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, os);
            fos.write(os.toByteArray());
            fos.close();

            Uri uri = FileProvider.getUriForFile(this, "ca.ualberta.cs.wrkify.fileprovider", f);
            i.setData(uri);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(i);

        } catch (IOException e) {
            Log.e("EditTaskActivity", e.toString());
        }


    }

    /**
     * Finishes the activity with RESULT_TASK_DELETED.
     * This should signal the parent activity to delete the task.
     */
    private void deleteAndFinish() {
        if (true) {
            task.deleteAllImages();
            imageManager = new ImageManager();
            saveAndFinish();
            return;
        }

        WrkifyClient.getInstance().delete(this.task);
        setResult(RESULT_TASK_DELETED);
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        finish();
    }

    /**
     * Writes changes to the task and finishes the activity.
     * Returns RESULT_TASK_CREATED if this is a new task, or
     * else RESULT_OK if the task exists and has been edited.
     */
    private void saveAndFinish() {
        imageManager.save(task);

        View focus = getCurrentFocus();
        if (focus != null) { focus.clearFocus(); }

        if (this.taskIsNew) {
            this.task = WrkifyClient.getInstance()
                    .create(Task.class,
                            this.titleField.getText().toString(),
                            Session.getInstance(this).getUser(),
                            this.descriptionField.getText().toString()
                    );
            task.setCheckList(this.checkList);
        } else {
            task.setTitle(titleField.getText().toString());
            task.setDescription(descriptionField.getText().toString());
            WrkifyClient.getInstance().upload(this.task);
        }

        if (task == null) return;

        Intent intent = getIntent();
        intent.putExtra(EXTRA_RETURNED_TASK, this.task);

        if (taskIsNew) setResult(RESULT_TASK_CREATED, intent);
        else setResult(RESULT_OK, intent);

        finish();
    }

    private void showChecklistEditor() {
        checkListEditorView.setVisibility(View.VISIBLE);
        checkListAddButton.setVisibility(View.VISIBLE);
        checkListNewButton.setVisibility(View.GONE);
    }

    private void hideChecklistEditor() {
        checkListEditorView.setVisibility(View.GONE);
        checkListAddButton.setVisibility(View.GONE);
        checkListNewButton.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
