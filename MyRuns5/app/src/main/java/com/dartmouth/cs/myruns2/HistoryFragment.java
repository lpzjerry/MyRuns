package com.dartmouth.cs.myruns2;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.util.Log;

import java.util.ArrayList;


public class HistoryFragment extends Fragment {
    private View rootView;
    private ListView listView;
    private ArrayList<Record> records;
    private ArrayAdapter<Record> recordArrayAdapter;
    private RecordDataSource recordDataSource = MainActivity.recordDataSource;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public void updateList() {
        recordDataSource.open();
        records = recordDataSource.getAllRecords();
        recordDataSource.close();
        recordArrayAdapter = new RecordArrayAdapter(getActivity(), records);
        listView = rootView.findViewById(R.id.history_list);
        listView.setAdapter(recordArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                Record record = records.get(position);
                Bundle bundle = new Bundle();
                bundle.putLong("ID", record.getId());
                bundle.putString("activityType", record.getType());
                bundle.putString("dateAndTime", record.getDateAndTime());
                bundle.putString("duration", record.getDurationStr());
                bundle.putString("distance", record.getDistanceStr());
                bundle.putString("calories", record.getCaloriesStr());
                bundle.putString("heartRate", record.getHeartRateStr());
                Intent intent;
                if (!record.getHeartRateStr().contains("#")) {
                    intent = new Intent(getActivity(), DisplayEntryActivity.class);
                } else {
                    intent = new Intent(getActivity(), DisplayGPSActivity.class);
                }
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_history, container, false);
        recordDataSource.open();
        records = recordDataSource.getAllRecords();
        recordDataSource.close();
        recordArrayAdapter = new RecordArrayAdapter(getActivity(), records);
        listView = rootView.findViewById(R.id.history_list);
        listView.setAdapter(recordArrayAdapter);
        return rootView;
    }

}