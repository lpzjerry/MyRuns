package com.xd.demoactiontabs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.text.format.DateUtils;
import android.widget.TimePicker;
import android.widget.DatePicker;

public class ManualEntryActivity extends ListActivity {
    Calendar mDateAndTime = Calendar.getInstance();
    // FragmentManager fragmentManager = getSupportFragmentManager();

    static final String[] inputMethod = new String[] {
            "Date", "Time", "Duration", "Distance", "Calories", "Heart Rate", "Comment"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, R.layout.listview_item, inputMethod);
        setListAdapter(mAdapter);

        OnItemClickListener mListener = new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String listViewText = ((TextView) view).getText().toString();
                Toast.makeText(getApplicationContext(), listViewText, Toast.LENGTH_SHORT).show();
                if (listViewText.equals("Date")) {
                    DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            mDateAndTime.set(Calendar.YEAR, year);
                            mDateAndTime.set(Calendar.MONTH, monthOfYear);
                            mDateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        }
                    };
                    new DatePickerDialog(ManualEntryActivity.this, mDateListener,
                            mDateAndTime.get(Calendar.YEAR),
                            mDateAndTime.get(Calendar.MONTH),
                            mDateAndTime.get(Calendar.DAY_OF_MONTH)).show();
                } else if (listViewText.equals("Time")) {
                    TimePickerDialog.OnTimeSetListener mTimeListener = new TimePickerDialog.OnTimeSetListener() {
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            mDateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            mDateAndTime.set(Calendar.MINUTE, minute);
                        }
                    };
                    new TimePickerDialog(ManualEntryActivity.this, mTimeListener,
                            mDateAndTime.get(Calendar.HOUR_OF_DAY),
                            mDateAndTime.get(Calendar.MINUTE), true).show();
                } else if (listViewText.equals("Comment")) {
                    // TODO: comment
                } else {
                    // TODO: add dialogGeneral

                }
            }
        };
        ListView listView = getListView();
        listView.setOnItemClickListener(mListener);
    }

    private void onItemClick(View view){
        DialogGeneral myDialog = new DialogGeneral();
        Bundle bundle = new Bundle();
        bundle.putInt(DialogGeneral.DIALOG_KEY, DialogGeneral.TEST_DIALOG);
        myDialog.setArguments(bundle);
        // myDialog.show(fragmentManager,"my dialog");
    }
    public void onClickButtonCancel(View view) {
        ManualEntryActivity.this.finish();
    }
}
