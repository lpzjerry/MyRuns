package edu.dartmouth.cs.myruns;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.content.Context;
import android.widget.EditText;
import android.app.ActionBar;
import android.widget.RadioButton;
import androidx.appcompat.widget.Toolbar;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyRuns";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Context context = getApplicationContext();
        // Name
        String userdata_name = readFromFile("userdata_name.txt", context);
        EditText editName = (EditText) findViewById(R.id.name_input);
        editName.setText(userdata_name);
        // Email
        String userdata_email = readFromFile("userdata_email.txt", context);
        EditText editEmail = (EditText) findViewById(R.id.email_input);
        editEmail.setText(userdata_email);
        // Phone
        String userdata_phone = readFromFile("userdata_phone.txt", context);
        EditText editPhone = (EditText) findViewById(R.id.phone_input);
        editPhone.setText(userdata_phone);
        // Class
        String userdata_class = readFromFile("userdata_class.txt", context);
        EditText editClass = (EditText) findViewById(R.id.class_input);
        editClass.setText(userdata_class);
        // Major
        String userdata_major = readFromFile("userdata_major.txt", context);
        EditText editMajor = (EditText) findViewById(R.id.major_input);
        editMajor.setText(userdata_major);
        // Gender
        String userdata_gender = readFromFile("userdata_gender.txt", context);
        RadioButton radio_female = (RadioButton) findViewById(R.id.radio_female);
        RadioButton radio_male = (RadioButton) findViewById(R.id.radio_male);
        if (userdata_gender.equals("Female")) {
            radio_female.setChecked(true);
            radio_male.setChecked(false);
        }
        else if (userdata_gender.equals("Male")) {
            radio_female.setChecked(false);
            radio_male.setChecked(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_bar, menu);
        return true;
    }

    public void onChangePhotoClicked(View view) {
        // TODO Implement
    }

    public void onSaveClick(View view) {
        Context context = getApplicationContext();
        // Name
        EditText textName = (EditText) findViewById(R.id.name_input);
        String strName = textName.getText().toString();
        Log.d(TAG, strName);
        writeToFile("userdata_name.txt", strName, context);
        // Email
        EditText textEmail = (EditText) findViewById(R.id.email_input);
        String strEmail = textEmail.getText().toString();
        Log.d(TAG, strEmail);
        writeToFile("userdata_email.txt", strEmail, context);
        // Phone
        EditText textPhone = (EditText) findViewById(R.id.phone_input);
        String strPhone = textPhone.getText().toString();
        Log.d(TAG, strPhone);
        writeToFile("userdata_phone.txt", strPhone, context);
        // Class
        EditText textClass = (EditText) findViewById(R.id.class_input);
        String strClass = textClass.getText().toString();
        Log.d(TAG, strClass);
        writeToFile("userdata_class.txt", strClass, context);
        // Major
        EditText textMajor = (EditText) findViewById(R.id.major_input);
        String strMajor = textMajor.getText().toString();
        Log.d(TAG, strMajor);
        writeToFile("userdata_major.txt", strMajor, context);
        // TODO: Gender
        String strGender = "";
        RadioButton radio_female = (RadioButton) findViewById(R.id.radio_female);
        RadioButton radio_male = (RadioButton) findViewById(R.id.radio_male);
        if (radio_female.isChecked()) {
            strGender = "Female";
        }
        else if (radio_male.isChecked()) {
            strGender = "Male";
        }
        writeToFile("userdata_gender.txt", strGender, context);
        MainActivity.this.finish();
    }

    public void onCancelClick(View view) {
        MainActivity.this.finish();
    }

    private String readFromFile(String fileName, Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(fileName);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.d("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.d("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private void writeToFile(String fileName, String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
