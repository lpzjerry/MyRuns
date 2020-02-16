package com.dartmouth.cs.myruns2;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.soundcloud.android.crop.Crop;

public class ProfileActivity extends AppCompatActivity {

    public static int MODE = Context.MODE_PRIVATE;
    public static final String PREFERENCE_NAME = "Profile";
    private SharedPreferences sharedPreferences;

    public static final int REQUEST_CODE_TAKE_FROM_CAMERA = 1;
    private static final String URI_INSTANCE_STATE_KEY = "saved_uri";
    private String tempImgFileName = "profile_photo.png";

    public static final int REQUEST_CODE_PICK_IMAGE = 2;

    private Uri mImageCaptureUri;
    //private Uri tempImgUri;
    private ImageView IVProfile;
    private boolean isTakenFromCamera;

    private EditText ETName;
    private EditText ETEmail;
    private EditText ETPhone;
    private EditText ETClass;
    private EditText ETMajor;
    private RadioGroup RGGender;
    private RadioButton RBNFemale;
    private RadioButton RBNMale;

    private static final String TAG = "MyRuns";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("TAG","onCreate called.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        checkPermissions();


        IVProfile = findViewById(R.id.IVProfile);
        //IVProfile.setImageResource(R.drawable.default_profile);


        if(savedInstanceState != null) {
            mImageCaptureUri = savedInstanceState.getParcelable(URI_INSTANCE_STATE_KEY);
            IVProfile.setImageURI(mImageCaptureUri);
        }

        loadSnap();

        sharedPreferences = getSharedPreferences(PREFERENCE_NAME,MODE);

        ETName = findViewById(R.id.ETName);
        ETEmail = findViewById(R.id.ETEmail);
        ETPhone = findViewById(R.id.ETPhone);
        ETClass = findViewById(R.id.ETClass);
        ETMajor = findViewById(R.id.ETMajor);
        RGGender = findViewById(R.id.RGGender);
        RBNFemale = findViewById(R.id.RBNFemale);
        RBNMale = findViewById(R.id.RBNMale);

        loadProfile();

        //File tempImgFile = new File(getExternalFilesDir(null), tempImgFileName);//XD: try Environment.DIRECTORY_PICTURES instead of "null"
        //mImageCaptureUri = FileProvider.getUriForFile(this, "com.xiangxinkong.myruns", tempImgFile);
        //IVProfile.setImageURI(mImageCaptureUri);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(URI_INSTANCE_STATE_KEY, mImageCaptureUri);
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState called.");
    }

    private void checkPermissions() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);
        }
    }

    private void loadSnap() {
        try {
            FileInputStream fis = openFileInput("profile_photo.png");
            Bitmap bmap = BitmapFactory.decodeStream(fis);
            IVProfile.setImageBitmap(bmap);
            Log.d("TAG","loadSnap called");
            fis.close();


        } catch (IOException e) {
            // Default profile photo if no photo saved before.
            IVProfile.setImageURI(mImageCaptureUri);
            //IVProfile.setImageResource(R.drawable.default_profile);
        }
        //IVProfile.setImageURI(mImageCaptureUri);
    }

    private void saveSnap() {
// Commit all the changes into preference file
        // Save profile image into internal storage.
        IVProfile.buildDrawingCache();
        Bitmap bmap = IVProfile.getDrawingCache();
        try {
            FileOutputStream fos = openFileOutput(
                    "profile_photo.png", MODE_PRIVATE);
            bmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            IVProfile.setImageURI(Crop.getOutput(result));
            //saveSnap();
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void changeButtonClicked(View view) {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Profile Picture");

        // add a list
        String[] items = {"Open Camera", "Select from Gallery"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        takePhotoFromCamera();
                        break;
                    case 1:
                        selectFromGallery();
                        break;

                }
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void loadProfile() {
        //String Picture = sharedPreferences.getString("Picture","");
        //mImageCaptureUri = Uri.parse(Picture);
        String Name = sharedPreferences.getString("Name","");
        String Email = sharedPreferences.getString("Email","");
        String Phone = sharedPreferences.getString("Phone","");
        int Class_int = sharedPreferences.getInt("Class",-1);

        String Major = sharedPreferences.getString("Major","");
        int Gender = sharedPreferences.getInt("Gender",-1);
        ETName.setText(Name);
        ETEmail.setText(Email);
        ETPhone.setText(Phone);
        if(Class_int != -1) {
              String class_string = String.valueOf(Class_int);
              ETClass.setText(class_string);
        }
        ETMajor.setText(Major);
      /*  if(Gender!=-1) {
            RadioButton BNToBeSet = findViewById(Gender);
            BNToBeSet.setChecked(true);
        }*/


    }

    private void saveProfile() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.putString("Picture", mImageCaptureUri.toString());
        editor.putString("Name", ETName.getText().toString());
        editor.putString("Email", ETEmail.getText().toString());
        editor.putString("Phone", ETPhone.getText().toString());

        String class_string = ETClass.getText().toString();
        int class_int = Integer.parseInt(class_string);
        editor.putInt("Class",class_int);

        editor.putString("Major", ETMajor.getText().toString());

        int genderID = RGGender.getCheckedRadioButtonId();
        editor.putInt("Gender", genderID);

        editor.apply();

        Log.d(TAG, "saveProfile called.");
    }

    public void saveButtonClicked(View view) {
        saveProfile();
        saveSnap();
        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "save button clicked.");
    }
    public void cancelButtonClicked(View view) {
        Log.d(TAG, "cancel button clicked.");
        finish();
    }

    // Handle data after activity returns.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, Integer.toString(resultCode));
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case REQUEST_CODE_TAKE_FROM_CAMERA:
                // Send image taken from camera for cropping
                beginCrop(mImageCaptureUri);

                break;
            case REQUEST_CODE_PICK_IMAGE:
                try {
                    mImageCaptureUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(mImageCaptureUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    IVProfile.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }

                beginCrop(mImageCaptureUri);
                break;
            case Crop.REQUEST_CROP:
                // Update image view after image crop
                handleCrop(resultCode, data);
                //saveSnap();
                // Delete temporary image taken by camera after crop.
                if (isTakenFromCamera) {
                    File f = new File(mImageCaptureUri.getPath());
                    if (f.exists())
                        f.delete();
                }

                break;
        }
        saveSnap();
    }

    public void takePhotoFromCamera() {
        Intent intent;

        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        ContentValues values = new ContentValues(1);
        values.put(MediaStore.Images.Media.MIME_TYPE,"image/jpg");
        mImageCaptureUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,mImageCaptureUri);
        intent.putExtra("return-data",true);
        try {
            //start a camera capturing activity
            startActivityForResult(intent, REQUEST_CODE_TAKE_FROM_CAMERA);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }

        isTakenFromCamera = true;
    }

    public void selectFromGallery() {
        Intent intent = new Intent();

        intent.setType("images/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_PICK_IMAGE);

        ContentValues values = new ContentValues(1);
        values.put(MediaStore.Images.Media.MIME_TYPE,"image/jpg");
        mImageCaptureUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,mImageCaptureUri);
        intent.putExtra("return-data",true);

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        //Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        //chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(pickIntent, REQUEST_CODE_PICK_IMAGE);


    }
}
