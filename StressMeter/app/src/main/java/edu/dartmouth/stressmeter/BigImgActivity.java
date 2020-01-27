package edu.dartmouth.stressmeter;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.util.Log;
import android.content.Context;
import android.content.Intent;


import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.util.Objects;

public class BigImgActivity extends AppCompatActivity {
    private int grid_id = 0;
    private int position = 0;
    private String filename = "stress_timestamp.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_img);

        Util.checkPermissions(this);
        ImageView imageView = findViewById(R.id.bigImgView);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            grid_id = bundle.getInt("grid_id");
            position = bundle.getInt("position");
        }
        int img_id = PSM.getGridById(grid_id+1)[position];
        Drawable drawable = getResources().getDrawable(img_id, null);
        imageView.setImageDrawable(drawable);
    }

    public void onClickButtonCancel(View view) {
        BigImgActivity.this.finish();
    }

    public void onClickButtonSubmit(View view) {
        String ret = readFromFile(this);
        long tsLong = System.currentTimeMillis() / 1000;
        String data = tsLong + "," + position + "\n";
        writeToFile(ret+data, this);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        BigImgActivity.this.finish();
    }

    /*
     * Ref: https://stackoverflow.com/questions/14376807/how-to-read-write-string-from-a-file-in-android
     * Path: /data/user/0/edu.dartmouth.stressmeter/files/stress_timestamp.csv
     * Access: View > Tool Windows > Device File Explorer; /data/data/[package-name]
     */
    private void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                    context.openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (Exception e) {
            Log.d("pengze", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(Context context) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(filename);
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString).append("\n");
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (Exception e) {
            Log.d("pengze", "File read failed: " + e.toString());
        }
        return ret;
    }
}
