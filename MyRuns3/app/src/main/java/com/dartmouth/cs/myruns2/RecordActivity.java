package com.dartmouth.cs.myruns2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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


public class RecordActivity extends AppCompatActivity {
    static final String[] ENTRIES = new String[] {
            "Date",
            "Time",
            "Duration",
            "Distance",
            "Calories",
            "Heart Rate",
            "Comment"
    };
    public static int mYear, mMonth, mDay, mHour, mMinute;
    Button BNSave;
    Button BNCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ENTRIES);

        ListView LVRecord = findViewById(R.id.LVRecord);

        LVRecord.setAdapter(arrayAdapter);

        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        mHour = ca.get(Calendar.HOUR_OF_DAY);
        mMinute = ca.get(Calendar.MINUTE);

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
            public void onItemClick(AdapterView<?> adapter, View v, int position,long arg3)
            {
                //String value = (String)adapter.getItemAtPosition(position);
                //Log.d("TAG", "test");
                switch(position) {
                    case 0:
                        new DatePickerDialog(RecordActivity.this, onDateSetListener,mYear, mMonth, mDay).show();
                        break;
                    case 1:
                        new TimePickerDialog(RecordActivity.this, onTimeSetListener, mHour, mMinute, false).show();
                        break;
                    case 2:
                        InputDialog("Duration", InputType.TYPE_CLASS_NUMBER);
                        break;
                    case 3:
                        InputDialog("Distance", InputType.TYPE_CLASS_NUMBER);
                        break;
                    case 4:
                        InputDialog("Calories",InputType.TYPE_CLASS_NUMBER);
                        break;
                    case 5:
                        InputDialog("Heart Rate",InputType.TYPE_CLASS_NUMBER);
                        break;
                    case 6:
                        InputDialog("Comments",InputType.TYPE_CLASS_TEXT);
                        break;

                }
            }
        });

        BNSave = findViewById(R.id.BNRecordSave);
        BNCancel = findViewById(R.id.BNRecordCancel);


    }

    public void InputDialog(String title, int input_type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RecordActivity.this);
        builder.setTitle(title);
        final EditText input = new EditText(RecordActivity.this);
        input.setInputType(input_type);
        builder.setView(input);
        builder.setPositiveButton("OK",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

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
        finish();//temparorily
    }

    public void onCancelButtonClicked(View view) {
        finish();
    }
}
