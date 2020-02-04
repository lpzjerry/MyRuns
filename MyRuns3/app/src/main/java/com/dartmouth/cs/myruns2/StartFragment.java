package com.dartmouth.cs.myruns2;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment {

    Button BNStart;
    boolean start_manual_activity;

    String TAG;

    public StartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_start, container, false);
        BNStart = v.findViewById(R.id.BNStart);

        Spinner spinner = (Spinner)v.findViewById(R.id.SPInputType);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String input_method = parent.getSelectedItem().toString();
                Log.d("TAG",input_method);
                if(input_method.equals("Manual Entry"))
                    start_manual_activity = true;
                else
                    start_manual_activity = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        BNStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                Intent intent;

                if(start_manual_activity)
                    intent = new Intent(getActivity(), RecordActivity.class);
                else
                    intent = new Intent(getActivity(), MapActivity.class);

                startActivity(intent);
            }
        });

        return v;
    }

    //public void onStartClicked(View view) {
     //   Intent intent = new Intent(getActivity(), RecordActivity.class);
     //   startActivity(intent);
   // }

}