package com.dartmouth.cs.myruns2;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

/*
Reference: https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView
 */

public class RecordArrayAdapter extends ArrayAdapter<Record> {
    public RecordArrayAdapter(Context context, ArrayList<Record> records) {
        super(context, 0, records);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Record record = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_record, parent, false);
        }
        TextView tvBold = (TextView) convertView.findViewById(R.id.tvBoldText);
        TextView tvNormal = (TextView) convertView.findViewById(R.id.tvNormalText);
        tvBold.setText("Manual Entry: " + record.getType() + ", " + record.getDateAndTime());
        tvNormal.setText(record.getDistanceStr() + ", " + record.getDurationStr());
        // Return the completed view to render on screen
        return convertView;
    }
}