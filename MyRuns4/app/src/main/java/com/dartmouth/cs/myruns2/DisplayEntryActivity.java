package com.dartmouth.cs.myruns2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.text.InputType;
import android.util.Log;

public class DisplayEntryActivity extends AppCompatActivity {
    private String mInputType = "", mActivityType = "", mDateAndTime = "",
            mDuration = "", mDistance = "", mCalories = "", mHeartRate = "";
    private long id;

    private RecordDataSource recordDataSource = MainActivity.recordDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_entry);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getLong("ID");

            mInputType = "Manual Entry";
            EditText editText = (EditText) findViewById(R.id.ETInputType);
            editText.setHint(mInputType);
            editText.setHintTextColor(getColor(R.color.black));
            editText.setEnabled(false);
            editText.setInputType(InputType.TYPE_NULL);

            mActivityType = bundle.getString("activityType");
            editText = (EditText) findViewById(R.id.ETActivityType);
            editText.setHint(mActivityType);
            editText.setHintTextColor(getColor(R.color.black));
            editText.setEnabled(false);
            editText.setInputType(InputType.TYPE_NULL);

            mDateAndTime = bundle.getString("dateAndTime");
            editText = (EditText) findViewById(R.id.ETDateAndTime);
            editText.setHint(mDateAndTime);
            editText.setHintTextColor(getColor(R.color.black));
            editText.setEnabled(false);
            editText.setInputType(InputType.TYPE_NULL);

            mDuration = bundle.getString("duration");
            editText = (EditText) findViewById(R.id.ETDuration);
            editText.setHint(mDuration+" secs");
            editText.setHintTextColor(getColor(R.color.black));
            editText.setEnabled(false);
            editText.setInputType(InputType.TYPE_NULL);

            mDistance = bundle.getString("distance");
            editText = (EditText) findViewById(R.id.ETDistance);
            editText.setHint(mDistance);
            editText.setHintTextColor(getColor(R.color.black));
            editText.setEnabled(false);
            editText.setInputType(InputType.TYPE_NULL);

            mCalories = bundle.getString("calories");
            editText = (EditText) findViewById(R.id.ETCalories);
            editText.setHint(mCalories);
            editText.setHintTextColor(getColor(R.color.black));
            editText.setEnabled(false);
            editText.setInputType(InputType.TYPE_NULL);

            mHeartRate = bundle.getString("heartRate");
            editText = (EditText) findViewById(R.id.ETHeartRate);
            editText.setHint(mHeartRate);
            editText.setHintTextColor(getColor(R.color.black));
            editText.setEnabled(false);
            editText.setInputType(InputType.TYPE_NULL);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_entry, menu);
        return true;
    }

    public void onClickDeleteEntry(MenuItem item) {
        // TODO delete the entry
        recordDataSource.open();
        recordDataSource.delete(id);
        recordDataSource.close();
        this.finish();
    }
}
