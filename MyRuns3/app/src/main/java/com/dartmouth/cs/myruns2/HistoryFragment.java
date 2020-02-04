package com.dartmouth.cs.myruns2;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class HistoryFragment extends Fragment {
    private ListView listView;
    private ArrayList<Record> records;
    private ArrayAdapter<Record> recordArrayAdapter;
    private RecordDataSource recordDataSource = new RecordDataSource(getActivity());

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        /*recordDataSource.open();
        records = recordDataSource.getAllRecords();
        recordDataSource.close();
        recordArrayAdapter = new RecordArrayAdapter(getActivity(), records);
        listView = rootView.findViewById(R.id.history_list);
        listView.setAdapter(recordArrayAdapter);*/
        return rootView;
    }
}
