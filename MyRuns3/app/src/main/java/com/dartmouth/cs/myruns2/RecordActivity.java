package com.dartmouth.cs.myruns2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.util.Log;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;

public class RecordActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    static final String[] ENTRIES = new String[]{
            "Date",
            "Time",
            "Duration",
            "Distance",
            "Calories",
            "Heart Rate",
            "Comment"
    };
    private RecordDataSource recordDataSource = MainActivity.recordDataSource;
    public static Calendar calendar = Calendar.getInstance();
    public static int mYear, mMonth, mDay, mHour, mMinute;
    public static String mType = "Running", mEditTextBuffer = "";
    public static int mDuration = 0, mCalories = 0, mHeartRate = 0;
    public static double mDistance = 0;
    Button BNSave;
    Button BNCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mType = bundle.getString("activityType");
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ENTRIES);
        ListView LVRecord = findViewById(R.id.LVRecord);
        LVRecord.setAdapter(arrayAdapter);

        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        final DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            }
        };

        final TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            }
        };

        LVRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                switch (position) {
                    case 0:
                        new DatePickerDialog(
                                RecordActivity.this, RecordActivity.this,
                                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)).show();
                        break;
                    case 1:
                        TimePickerDialog timePickerDialog = new TimePickerDialog(
                                RecordActivity.this, RecordActivity.this,
                                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                        timePickerDialog.show();
                        break;
                    case 2:
                        InputDialog("Duration", InputType.TYPE_CLASS_NUMBER);
                        break;
                    case 3:
                        InputDialog("Distance", InputType.TYPE_CLASS_NUMBER);
                        break;
                    case 4:
                        InputDialog("Calories", InputType.TYPE_CLASS_NUMBER);
                        break;
                    case 5:
                        InputDialog("Heart Rate", InputType.TYPE_CLASS_NUMBER);
                        break;
                    case 6:
                        InputDialog("Comments", InputType.TYPE_CLASS_TEXT);
                        break;
                }
            }
        });
        BNSave = findViewById(R.id.BNRecordSave);
        BNCancel = findViewById(R.id.BNRecordCancel);
    }

    public void InputDialog(final String title, int input_type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RecordActivity.this);
        builder.setTitle(title);
        final EditText input = new EditText(RecordActivity.this);
        input.setInputType(input_type);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO save the data entry
                mEditTextBuffer = input.getText().toString();
                if (mEditTextBuffer.equals(""))
                    mEditTextBuffer = "0";
                switch (title) {
                    case "Duration":
                        mDuration = Integer.parseInt(mEditTextBuffer);
                        break;
                    case "Distance":
                        int unit = MainActivity.getUnit();
                        if (unit == MainActivity.UNIT_MILES) {
                            mDistance = Double.parseDouble(mEditTextBuffer);
                        } else {
                            mDistance = Double.parseDouble(mEditTextBuffer) / MainActivity.KM_TO_MILES;
                        }
                        break;
                    case "Calories":
                        mCalories = Integer.parseInt(mEditTextBuffer);
                        break;
                    case "Heart Rate":
                        mHeartRate = Integer.parseInt(mEditTextBuffer);
                        break;
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void onSaveButtonClicked(View view) {
        // TODO save to database
        Log.d("pengze", "RecordActivity: getWritableDatabase");
        recordDataSource.open();
        recordDataSource.insert(mType, calendarToString(calendar), mDuration, mDistance, mCalories, mHeartRate);
        recordDataSource.close();
        finish();
    }

    public void onCancelButtonClicked(View view) {
        finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(year, month, dayOfMonth);
        mYear = year;
        mMonth = month;
        mDay = dayOfMonth;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(mYear, mMonth, mDay, hourOfDay, minute, 0);
        mHour = hourOfDay;
        mMinute = minute;
    }

    public static String calendarToString(Calendar calendar) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss MMM d yyyy");
        return format.format(calendar.getTime());
    }
}