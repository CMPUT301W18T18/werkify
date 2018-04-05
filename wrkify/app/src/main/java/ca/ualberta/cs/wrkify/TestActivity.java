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

import android.content.ContentProvider;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.apache.commons.lang3.SerializationUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Date;
import java.text.SimpleDateFormat;

public class TestActivity extends AppCompatActivity {
    //pieces of this adapted from https://developer.android.com/training/camera/photobasics.html
    protected static final int REQUEST_IMAGE_CAMERA = 1;
    protected static final int REQUEST_IMAGE_GALLERY = 2;


    protected ImageView thumbnailView;
    protected ImageView fullImageView;
    protected Menu menu;
    protected ActionMode.Callback callback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.test_menu_select, menu);
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

        }
    };

    //AWKUHg6kGjLoXk81quSg
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        thumbnailView = findViewById(R.id.testImageThumbnail);
        fullImageView = findViewById(R.id.testImageFull);

        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX);

        Button testButton = findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked();
            }
        });


        Button showButton = findViewById(R.id.testShow);
        Button hideButton = findViewById(R.id.testHide);

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDeleteVisibility(true);
            }
        });

        hideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDeleteVisibility(false);
            }
        });

        final Context curContext = this; //don't mind this for now lol...
        Button viewButton = findViewById(R.id.testView);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setType("image/*");

                try {
                    File dir = new File(getCacheDir(), "dataHere");
                    dir.mkdir();
                    File f = new File(dir, "sending.png");

                    FileOutputStream fos = new FileOutputStream(f);
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    curImage.compress(Bitmap.CompressFormat.PNG, 100, os);
                    fos.write(os.toByteArray());
                    fos.close();

                    Uri uri = FileProvider.getUriForFile(curContext, "ca.ualberta.cs.wrkify.fileprovider", f);
                    Log.i("URI IS", uri.toString());

                    i.setData(uri);
                    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(i);
                    /*
                    //File f = File.createTempFile("TEMPFILE",".png");
                    File dir = new File(curContext.getFilesDir(), "images");
                    File f = new File(dir, "file.png");
                    FileOutputStream fos = new FileOutputStream(f);

                    curImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    Uri uri = FileProvider.getUriForFile(curContext, "ca.ualberta.cs.wrkify.fileprovider", f);

                    Log.i("Filesize", Long.toString(f.length()));
                    Log.i("Uri is:", uri.toString());
                    i.setData(uri);
                    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    i.addFlags(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
                    i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    i.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                    startActivity(i);
                    */
                } catch (IOException e) {
                    Log.e("File failed", e.getStackTrace().toString());
                    e.printStackTrace();
                }
            }
        });


        try {
            CompressedBitmap dow = WrkifyClient.getInstance().download("AWKUHg6kGjLoXk81quSg", CompressedBitmap.class);
            RemoteReference<CompressedBitmap> meme =  dow.reference();

            CompressedBitmap last = meme.getRemote(WrkifyClient.getInstance(), CompressedBitmap.class);
            fullImageView.setImageBitmap(last.getBitmap());
            curImage = last.getBitmap();
        } catch (IOException e) {
            Log.e("Couldn't download", "---------------");

        }

    }

    Bitmap curImage;

    protected void setImages(Bitmap thumbnail, Bitmap fullImage) {
        thumbnailView.setImageBitmap(thumbnail);
        fullImageView.setImageBitmap(fullImage);
        Log.i("Size thumbnail:", "(" + thumbnail.getWidth() + ", " + thumbnail.getHeight() + ")");
        Log.i("Size full:", "(" + fullImage.getWidth() + ", " + fullImage.getHeight() + ")");



        CompressedBitmap cb = new CompressedBitmap(ImageUtilities.compressBitmapToBytes(fullImage, 65536, 10));
        Bitmap compressed = cb.getBitmap();
        fullImageView.setImageBitmap(compressed);
        curImage = compressed;



    }


    protected void compressMany(Bitmap in) {
        for (int i = 0; i <= 100; i++) {
            ByteArrayOutputStream str = new ByteArrayOutputStream();
            in.compress(Bitmap.CompressFormat.JPEG, i, str);
            ByteArrayInputStream stro = new ByteArrayInputStream(str.toByteArray());
            Bitmap out = BitmapFactory.decodeStream(stro);
            MediaStore.Images.Media.insertImage(getContentResolver(), out, "C_" + i, "description here");
        }
    }


    protected void setDeleteVisibility(boolean isVisible) {
        //MenuItem delete = menu.findItem(R.id.testDeleteButton);
        MenuItem delete = menu.findItem(R.id.testDeleteButton);
        //delete.setVisible(isVisible);
        if (isVisible == false) {
            startActionMode(callback);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.test_menu, menu);
        this.menu = menu;

        menu.findItem(R.id.testDeleteButton).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.i("--------", "PRESSED THE DELETE BUTTON");
                return true;
            }
        });

        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAMERA && resultCode == RESULT_OK) {
            //Bundle b = data.getExtras();
            //Bitmap thumbnail = (Bitmap) b.get("data");

            Log.i("intnet", data.toString());
            Bitmap thumbnail = null;
            Bitmap fullImage = null;
            try {
                Uri uri = Uri.fromFile(new File(currentPhotoPath));
                fullImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                if (fullImage != null) {
                    thumbnail = fullImage;
                    // https://stackoverflow.com/questions/8560501/android-save-image-into-gallery https://stackoverflow.com/a/8722494 (line below)
                    MediaStore.Images.Media.insertImage(getContentResolver(), fullImage, "filenameHere", "description here");
                } else {
                    Log.e("asdasd", "shit is null");
                }
            } catch (IOException e) {
                Log.e("Failed to get full", e.toString());
            }

            setImages(thumbnail, fullImage);
        } else if (requestCode == REQUEST_IMAGE_GALLERY) {
            Log.i("return intent", data.toString());
            Uri uri = data.getData();
            Bitmap bm = null;

            try {
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                Log.e("Doesn't work", e.toString());
            }
            if (bm != null) {
                setImages(bm, bm);
            }

        }
    }


    protected String currentPhotoPath;
    protected File makeNewImageFile() throws IOException {
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = "wrkify_" + time;
        String extension = ".jpg";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(filename, extension, storageDir);
        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    protected void openCamera() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (i.resolveActivity(getPackageManager()) != null) {
            File imageFile = null;

            try {
                imageFile = makeNewImageFile();
            } catch (IOException e) {
                Log.e("Failed to create image", e.toString());
            }

            if (imageFile != null) {
                Uri uri = FileProvider.getUriForFile(this, "ca.ualberta.cs.wrkify.fileprovider", imageFile);
                i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(i, REQUEST_IMAGE_CAMERA);
            }
        }
    }

    protected void openGallery() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i, REQUEST_IMAGE_GALLERY);

    }

    protected void buttonClicked() {
        //Show the dialog
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

}
