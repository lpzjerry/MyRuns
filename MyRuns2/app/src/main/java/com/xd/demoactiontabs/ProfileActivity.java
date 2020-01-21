package com.xd.demoactiontabs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.provider.MediaStore;
import android.view.View;
import android.view.Menu;
import android.widget.RadioGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.net.Uri;
import java.io.File;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.soundcloud.android.crop.Crop;

public class ProfileActivity extends AppCompatActivity {

    public static final int CAMERA_REQUEST_CODE = 1;
    private Uri tempImgUri;
    private ImageView imageView;
    private int photo_id = 0;
    String line = "...";//
    TextView textView;
    public static final String TEXTVIEW_KEY = "textview_key";
    public static final String IMAGEVIEW_KEY = "imageview_key";
    public static final String TAG = "DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textView = (TextView) findViewById(R.id.text_view);
        imageView = (ImageView) findViewById(R.id.imageProfile);
        loadUserData();
        if (savedInstanceState != null) {
            photo_id = savedInstanceState.getInt(IMAGEVIEW_KEY);
            File tempImgFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), getTempImgFileName());
            if (tempImgFile.exists()) {
                tempImgUri = FileProvider.getUriForFile(this, "com.xd.demoactiontabs", tempImgFile);
                imageView.setImageURI(tempImgUri);
                line = tempImgUri.getPath();
            }
            else {
                line = "...";
                tempImgUri = null;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt(IMAGEVIEW_KEY, photo_id);
        outState.putString(TEXTVIEW_KEY, line);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_bar, menu);
        return true;
    }

    public void onChangePhotoClicked(View view) {
        this.photo_id += 1;
        File tempImgFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), getTempImgFileName());
        Log.d("TAG-DEBUG-0", (String) tempImgFile.getAbsolutePath());
        tempImgUri = FileProvider.getUriForFile(this, "com.xd.demoactiontabs", tempImgFile);
        Log.d("TAG-DEBUG-1", (String) tempImgUri.toString());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempImgUri);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == CAMERA_REQUEST_CODE) {
            Crop.of(tempImgUri, tempImgUri).asSquare().start(this);
        } else if (requestCode == Crop.REQUEST_CROP) {
            Uri selectedImgUri = Crop.getOutput(data);
            imageView.setImageURI(null);
            imageView.setImageURI(selectedImgUri);
            line = tempImgUri.getPath();
        }
        Log.d(TAG, "RETURN: onActivityResult()");
    }

    public void onSaveClicked(View v) {
        saveUserData();
        ProfileActivity.this.finish();
    }

    public void onCancelClicked(View v) {
        ProfileActivity.this.finish();
    }

    private void loadUserData() {
        String mKey = getString(R.string.preference_name);
        SharedPreferences mPrefs = getSharedPreferences(mKey, MODE_PRIVATE);

        // Name
        mKey = getString(R.string.preference_key_profile_name);
        String mValueName = mPrefs.getString(mKey, " ");
        ((EditText) findViewById(R.id.editName)).setText(mValueName);
        // Email
        mKey = getString(R.string.preference_key_profile_email);
        String mValueEmail = mPrefs.getString(mKey, " ");
        ((EditText) findViewById(R.id.editEmail)).setText(mValueEmail);
        // Phone
        mKey = getString(R.string.preference_key_profile_phone);
        String mValuePhone = mPrefs.getString(mKey, " ");
        ((EditText) findViewById(R.id.editPhone)).setText(mValuePhone);
        // Class
        mKey = getString(R.string.preference_key_profile_class);
        String mValueClass = mPrefs.getString(mKey, " ");
        ((EditText) findViewById(R.id.editClass)).setText(mValueClass);
        // Major
        mKey = getString(R.string.preference_key_profile_major);
        String mValueMajor = mPrefs.getString(mKey, " ");
        ((EditText) findViewById(R.id.editMajor)).setText(mValueMajor);
        // Gender
        mKey = getString(R.string.preference_key_profile_gender);
        int mIntGender = mPrefs.getInt(mKey, -1);
        // In case there isn't one saved before:
        if (mIntGender >= 0) {
            // Find the radio button that should be checked.
            RadioButton radioBtn = (RadioButton) ((RadioGroup) findViewById(R.id.radioGender))
                    .getChildAt(mIntGender);
            // Check the button.
            radioBtn.setChecked(true);
        }
        // Photo
        mKey = "profile_photo";
        photo_id = mPrefs.getInt(mKey, 0);
        File tempImgFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), getTempImgFileName());
        if (tempImgFile.exists()) {
            tempImgUri = FileProvider.getUriForFile(this, "com.xd.demoactiontabs", tempImgFile);
            imageView.setImageURI(tempImgUri);
            line = tempImgUri.getPath();
        }
        else {
            line = "...";
            tempImgUri = null;
        }
    }

    private void saveUserData() {
        Log.d(TAG, "saveUserData()");
        // Getting the shared preferences editor
        String mKey = getString(R.string.preference_name);
        SharedPreferences mPrefs = getSharedPreferences(mKey, MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.clear();
        // Name
        mKey = getString(R.string.preference_key_profile_name);
        String mValueName = (String) ((EditText) findViewById(R.id.editName)).getText().toString();
        mEditor.putString(mKey, mValueName);
        // Email
        mKey = getString(R.string.preference_key_profile_email);
        String mValueEmail = (String) ((EditText) findViewById(R.id.editEmail)).getText().toString();
        mEditor.putString(mKey, mValueEmail);
        // Phone
        mKey = getString(R.string.preference_key_profile_phone);
        String mValuePhone = (String) ((EditText) findViewById(R.id.editPhone)).getText().toString();
        mEditor.putString(mKey, mValuePhone);
        // Class
        mKey = getString(R.string.preference_key_profile_class);
        String mValueClass = (String) ((EditText) findViewById(R.id.editClass)).getText().toString();
        mEditor.putString(mKey, mValueClass);
        // Major
        mKey = getString(R.string.preference_key_profile_major);
        String mValueMajor = (String) ((EditText) findViewById(R.id.editMajor)).getText().toString();
        mEditor.putString(mKey, mValueMajor);
        // Gender
        mKey = getString(R.string.preference_key_profile_gender);
        RadioGroup mRadioGroup = (RadioGroup) findViewById(R.id.radioGender);
        int mIntGender = mRadioGroup.indexOfChild(findViewById(mRadioGroup.getCheckedRadioButtonId()));
        mEditor.putInt(mKey, mIntGender);
        // Photo
        mKey = "profile_photo";
        int mIntPhoto = photo_id;
        mEditor.putInt(mKey, mIntPhoto);
        // Commit all the changes into the shared preference
        mEditor.apply();
    }

    private String getTempImgFileName() {
        return Integer.toString(this.photo_id) + ".jpg";
    }
}