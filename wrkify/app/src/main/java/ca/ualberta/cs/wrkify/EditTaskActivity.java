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
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

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
    /**
     *Class for separating local and remote images
     */
    public class ImageManager {
        private ArrayList<RemoteReference<CompressedBitmap>> remoteImages;
        private ArrayList<CompressedBitmap> localImages;
        private ArrayList<CompressedBitmap> thumbnails;
        private ArrayList<RemoteReference<CompressedBitmap>> toDelete;

        /**
         * Constructor for ImageManager, initializes all the lists
         */
        public ImageManager() {
            remoteImages = new ArrayList<>();
            localImages = new ArrayList<>();
            thumbnails = new ArrayList<>();
            toDelete = new ArrayList<>();
        }

        public int getLocalThumbnailStartId() {
            return remoteImages.size();
        }

        public int getLocalImagesSize() {
            return localImages.size();
        }

        public CompressedBitmap[] getLocalPair(int index) {
            CompressedBitmap[] arr = new CompressedBitmap[2];
            int offset = getLocalThumbnailStartId();
            arr[0] = thumbnails.get(index + offset);
            arr[1] = localImages.get(index);
            return arr;
        }

        /**
         * @param list ArrayList of RemoteReferences to CompressedBitmaps
         */
        public void setRemoteImages(ArrayList<RemoteReference<CompressedBitmap>> list) {
            this.remoteImages = list;
        }

        /**
         * @param thumbnails ArrayList of CompressedBitmaps representing thumbnails
         */
        public void setThumbnails(ArrayList<CompressedBitmap> thumbnails) { //not documented (DELETE THE PRINTS)
            Log.i("Set thumbs list", "Size: " + thumbnails.size());
            this.thumbnails = thumbnails;
            for (int i = 0; i < thumbnails.size(); i++) {
                Log.i("Set thumbs list", thumbnails.get(i) + "");
            }
        }

        /**
         * Add an image to the list, as a (thumbnail, image) pair
         *
         * @param thumbnail CompressedBitmap representing thumbnail
         * @param image     CompressedBitmap representing full-size image
         */
        public void addImagePair(CompressedBitmap thumbnail, CompressedBitmap image) {
            localImages.add(image);
            thumbnails.add(thumbnail);
        }

        /**
         * Deletes images specified by the IDs in the list. Local images are deleted, while
         * remote images are removed from the list and queued for deletion upon saving changes
         *
         * @param idList ArrayList of IDs whose images you want to delete from the list
         */
        public void deleteFromIds(ArrayList<Integer> idList) {
            Collections.sort(idList);

            for (int i = idList.size() - 1; i >= 0; i--) {
                int id = idList.get(i);

                if (id < remoteImages.size()) {
                    toDelete.add(remoteImages.get(id));
                    remoteImages.remove(id);
                    toDelete.add(thumbnails.get(id).<CompressedBitmap>reference());
                    thumbnails.remove(id);
                } else {
                    thumbnails.remove(id);
                    localImages.remove(id - remoteImages.size());
                }
            }
        }

        /**
         * @param index Position of the image you want to get
         * @return CompressedBitmap representing full-size image
         */
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

        /**
         * Applies queued deletions, then uploads local images
         *
         * @param task
         */
        public void save(Task task) {
            flushDeletions();
        }

        /**
         * Deletes remote image from the server, where deletions were queued
         */
        private void flushDeletions() {
            for (int i = 0; i < toDelete.size(); i++) {
                try {
                    WrkifyClient.getInstance().delete(toDelete.get(i).getRemote(WrkifyClient.getInstance(), CompressedBitmap.class));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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

    /** The task was edited, but the changes were not synced successfully. */
    public static final int RESULT_UNSYNCED_CHANGES = 20;


    private Task task;
    private CheckList checkList;
    private boolean taskIsNew = false;

    private EditText titleField;
    private EditText descriptionField;
    private CheckListEditorView checkListEditorView;
    private Button checkListNewButton;
    private Button checkListAddButton;

    //--------------------------------------------------------------------------------
    private RecyclerView recyclerView;
    private ImageManager imageManager;
    private TaskImageListAdapter adapter;
    private ActionMode currentAction;
    private String currentImagePath;
    private boolean alreadySaved = false;

    private static final int REQUEST_IMAGE_CAMERA = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        ActionBar actionBar = getSupportActionBar();

        this.titleField = findViewById(R.id.editTaskTitleField);
        this.descriptionField = findViewById(R.id.editTaskDescriptionField);
        this.checkListEditorView = findViewById(R.id.editTaskChecklist);
        this.checkListNewButton = findViewById(R.id.editTaskButtonChecklistNew);
        this.checkListAddButton = findViewById(R.id.editTaskButtonChecklistAdd);

        //Set up image stuff
        imageManager = new ImageManager();
        recyclerView = findViewById(R.id.editTaskImageRecyclerView);
        adapter = new TaskImageListAdapter(imageManager.thumbnails) {
            @Override
            public void buttonClicked(int position) {
                if (currentAction == null) {
                    showImageAt(position);
                } else {
                    toggleSelected(position);
                    updateSelectionCount();
                }
            }

            @Override
            public void buttonLongClicked(int position) {
                if (currentAction == null) {
                    startSelectionMode();
                    toggleSelected(position);
                    updateSelectionCount();
                }
            }
        };
        this.task = (Task) getIntent().getSerializableExtra(EXTRA_EXISTING_TASK);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();

        this.new DownloadImagesTask().execute();

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_task, menu);

        MenuItem saveItem = menu.findItem(R.id.menuItemSaveTask);
        MenuItem deleteItem = menu.findItem(R.id.menuItemDeleteTask);
        MenuItem addImage = menu.findItem(R.id.menuItemAddPhoto);

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

        addImage.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                clickedAddPhoto();
                return true;
            }
        });

        return true;
    }

    /**
     * When the delete action mode is active, updates the count text in the action bar
     */
    private void updateSelectionCount() {
        if (currentAction != null) {
            if (adapter.numberSelected() == 0) {
                currentAction.finish();
                return;
            }
            currentAction.setTitle(adapter.numberSelected() + " selected");
        }
    }

    private void startSelectionMode() {
        ActionMode.Callback callback = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(final ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.deletion_menu, menu);
                MenuItem deleteButton = menu.findItem(R.id.deleteImagesMenuButton);
                deleteButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        imageManager.deleteFromIds(adapter.getSelectedIds());
                        adapter.animateDeletions();
                        mode.finish();
                        return true;
                    }
                });
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adapter.deselectAll();
                currentAction = null;

            }
        };
        currentAction = startActionMode(callback);
    }

    /**
     * Opens alert dialog asking for an image source (camera or gallery)
     */
    private void clickedAddPhoto() {
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

    /**
     * @return new Image file that you can write to
     * @throws IOException
     */
    private File makeNewImage() throws IOException {
        //Taken from https://developer.android.com/training/camera/photobasics.html
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = "wrkify_" + time;
        String extension = ".jpg";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(filename, extension, storageDir);
        currentImagePath = imageFile.getAbsolutePath();
        return imageFile;
    }

    /**
     * Requests a new image from the camera app, launches the app
     */
    private void openCamera() {
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

    /**
     * Requests a new image from the gallery app, launches the app
     */
    private void openGallery() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        if (i.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(i, REQUEST_IMAGE_GALLERY);
        }
    }

    private void showImageAt(int id) {
        this.new ShowImageTask().execute(id);
    }

    /**
     * Opens the specified image in a viewing app
     * @param position Position of image you want to view
     */
    private void showImage(CompressedBitmap cb) {
        Bitmap bm = null;
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
        this.new DeleteTaskTask().execute();
    }

    /**
     * Writes changes to the task and finishes the activity.
     * Returns RESULT_TASK_CREATED if this is a new task, or
     * else RESULT_OK if the task exists and has been edited.
     */
    private void saveAndFinish() {
        if (alreadySaved) {
            return;
        }

        alreadySaved = true;
        boolean valid = true;

        View focus = getCurrentFocus();
        if (focus != null) { focus.clearFocus(); }

        try {
            Task.verifyTitle(titleField.getText().toString());
        } catch (IllegalArgumentException e) {
            titleField.setError(e.getMessage());
            valid = false;
        }

        try {
            Task.verifyDescription(descriptionField.getText().toString());
        } catch (IllegalArgumentException e) {
            descriptionField.setError(e.getMessage());
            valid = false;
        }

        try {
            Task.verifyChecklist(checkList);
        } catch (IllegalArgumentException e) {
            Snackbar snack = Snackbar.make(findViewById(android.R.id.content),
                    e.getMessage(), Snackbar.LENGTH_LONG);
            snack.setAction("Action", null);
            snack.show();
            valid = false;
        }

        if (!valid) { return; }

        this.new EditTaskTask().execute();
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
                    CompressedBitmap compressedImage = ImageUtilities.makeCompressedImage(image, false);
                    CompressedBitmap compressedThumbnail = ImageUtilities.makeCompressedThumbnail(image, false);

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
                CompressedBitmap compressedImage = ImageUtilities.makeCompressedImage(bm, false);
                CompressedBitmap compressedThumbnail = ImageUtilities.makeCompressedThumbnail(bm, false);
                imageManager.addImagePair(compressedThumbnail, compressedImage);
                adapter.notifyDataSetChanged();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * EditTaskTask is an AsyncTask for completing the editing
     * of a task.
     */
    private class EditTaskTask extends AsyncTask<Void, Void, Void> {
        private int resultCode;
        /**
         * change the task and upload it
         * to the client
         * @param voids unused
         * @return unused
         */
        @Override
        protected Void doInBackground(Void... voids) {
            String newTitle = titleField.getText().toString();
            String newDescription = descriptionField.getText().toString();

            if (taskIsNew) {
                // TODO This will probably fail if offline
                task = (Task) WrkifyClient.getInstance().createLocal(Task.class,
                        titleField.getText().toString(),
                        Session.getInstance(EditTaskActivity.this).getUser(),
                        descriptionField.getText().toString()
                );
                task.setCheckList(checkList);
                uploadAndAttachImages();

                TransactionManager transactionManager = Session.getInstance(EditTaskActivity.this).getTransactionManager();
                transactionManager.enqueue(new TaskCreateTransaction(task));
                WrkifyClient.getInstance().updateCached(task);

                transactionManager.flush(WrkifyClient.getInstance());

                resultCode = RESULT_TASK_CREATED;
            } else {
                TransactionManager transactionManager = Session.getInstance(EditTaskActivity.this).getTransactionManager();

                uploadAndAttachImages();

                transactionManager.enqueue(new TaskTitleTransaction(task, newTitle));
                transactionManager.enqueue(new TaskDescriptionTransaction(task, newDescription));
                transactionManager.enqueue(new TaskCheckListTransaction(task, checkList));

                task.setTitle(newTitle);
                task.setDescription(newDescription);
                task.setCheckList(checkList);
                WrkifyClient.getInstance().updateCached(task);

                if (transactionManager.flush(WrkifyClient.getInstance())) {
                    resultCode = RESULT_OK;
                } else {
                    resultCode = RESULT_UNSYNCED_CHANGES;
                }
            }
            return null;
        }

        public void uploadAndAttachImages() { //TODO split this into more functions, make it work too
            TransactionManager transactionManager = Session.getInstance(EditTaskActivity.this).getTransactionManager();

            for (int i = 0; i < imageManager.getLocalImagesSize(); i++) {
                CompressedBitmap[] arr = imageManager.getLocalPair(i);

                transactionManager.enqueue(new ImageUploadTransaction(arr[0])); //thumb
                transactionManager.enqueue(new ImageUploadTransaction(arr[1])); //image

                transactionManager.enqueue(new ImageAttachTransaction(task, arr[0].<CompressedBitmap>reference(), arr[1].<CompressedBitmap>reference()));
                task.addImagePair(arr[0].<CompressedBitmap>reference(), arr[1].<CompressedBitmap>reference());
                Log.i("Added pair", "" + arr[0].reference() + " " + arr[1].reference());
            }
            WrkifyClient.getInstance().updateCached(task);

        }

        /**
         * when the task has been uploaded, if it was sucessful,
         * finish the activity properly
         * @param result unused
         */
        @Override
        protected void onPostExecute(Void result) {
            if (task == null) return;

            Intent intent = getIntent();
            intent.putExtra(EXTRA_RETURNED_TASK, task);
            setResult(resultCode, intent);
            WrkifyClient.getInstance().updateCached(task);

            finish();
        }
    }

    private class DownloadImagesTask extends AsyncTask<Void, Void, Void> { //not documented
        private int resultCode;
        /**
         * change the task and upload it
         * to the client
         * @param voids unused
         * @return unused
         */
        @Override
        protected Void doInBackground(Void... voids) {
            if (task == null) {
                return null;
            }
            ArrayList<CompressedBitmap> thumbs = imageManager.thumbnails;
            ArrayList<RemoteReference<CompressedBitmap>> references = task.getRemoteThumbnails();

            Log.i("Downloading thumbnails", "" + references.size());

            for (int i = 0; i < references.size(); i++) {
                try {
                    Log.i("Found in references", "" + references.get(i).getRefId());
                    String id = references.get(i).getRefId();
                    id = WrkifyClient.getInstance().canonicalize(id);

                    CompressedBitmap bitmap = (CompressedBitmap) WrkifyClient.getInstance().download(id, CompressedBitmap.class);
                    Log.i("GOT THUMBNAIL:", "" + bitmap);
                    thumbs.add(bitmap);
                } catch (IOException e) {
                    Log.e("DownloadImagesTask", "Failed to get thumbnails");
                }
            }

            imageManager.setThumbnails(thumbs);
            Log.i("DOWNLOADED THUMBS", "" + thumbs);
            return null;
        }

        /**
         * when the task has been uploaded, if it was sucessful,
         * finish the activity properly
         * @param result unused
         */
        @Override
        protected void onPostExecute(Void result) {
            if (task == null) {
                return;
            }
            imageManager.setRemoteImages(task.getRemoteImages());
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * DeleteTaskTask is an AsyncTask that deletes the given task
     * and returns.
     */
    public class DeleteTaskTask extends AsyncTask<Void, Void, Void> {
        /**
         * delete the task from the server
         * @param voids unused
         * @return unused
         */
        @Override
        protected Void doInBackground(Void... voids) {
            TransactionManager transactionManager = Session.getInstance(EditTaskActivity.this).getTransactionManager();
            transactionManager.enqueue(new TaskDeleteTransaction(task));

            WrkifyClient.getInstance().discardCached(task.getId());

            // TODO notify of offline status
            transactionManager.flush(WrkifyClient.getInstance());
            return null;
        }

        /**
         * after the task has been delete from the server,
         * return to the correct activity.
         * @param result unused
         */
        @Override
        protected void onPostExecute(Void result) {


            setResult(RESULT_TASK_DELETED);
            View view = EditTaskActivity.this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            finish();
        }
    }

    private class ShowImageTask extends AsyncTask<Object, Void, CompressedBitmap> { //not documented
        private int resultCode;
        /**
         * change the task and upload it
         * to the client
         * @param id int, id of image to view
         * @return unused
         */
        @Override
        protected CompressedBitmap doInBackground(Object... id) {
            int i = (Integer) id[0];
            try {
                String canonicalId = imageManager.remoteImages.get(i).getRefId();
                canonicalId = WrkifyClient.getInstance().canonicalize(canonicalId);

                CompressedBitmap bm = (CompressedBitmap) WrkifyClient.getInstance().download(canonicalId, CompressedBitmap.class);
                return bm;
            } catch (IOException e) {
                Log.i("ShowImageTask", "Failed to download full image");
                return null;
            }
        }

        /**
         * when the task has been uploaded, if it was sucessful,
         * finish the activity properly
         * @param result unused
         */
        @Override
        protected void onPostExecute(CompressedBitmap result) {
            showImage(result);
        }


    }

}
