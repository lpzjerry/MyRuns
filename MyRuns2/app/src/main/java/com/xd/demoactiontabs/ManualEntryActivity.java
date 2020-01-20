package com.xd.demoactiontabs;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.view.View;

public class ManualEntryActivity extends ListActivity {
    static final String[] inputMethod = new String[] {
            "Date", "Time", "Duration", "Distance", "Calories", "Heart Rate", "Comment"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, inputMethod);
        setListAdapter(mAdapter);

        OnItemClickListener mListener = new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String listViewText = ((TextView) view).getText().toString();
                Toast.makeText(getApplicationContext(), listViewText, Toast.LENGTH_SHORT).show();
                if (listViewText.equals("Date")) {
                    // TODO: date pick
                } else if (listViewText.equals("Time")) {
                    // TODO: clock
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

    public void onClickButtonCancel(View view) {
        ManualEntryActivity.this.finish();
    }
}
