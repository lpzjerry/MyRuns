package edu.dartmouth.stressmeter.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.GridView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import android.content.Intent;

import edu.dartmouth.stressmeter.BigImgActivity;
import edu.dartmouth.stressmeter.GridViewAdapter;
import edu.dartmouth.stressmeter.MainActivity;
import edu.dartmouth.stressmeter.PSM;
import edu.dartmouth.stressmeter.R;

public class HomeFragment extends Fragment {
    private static int gridImgID = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        GridView gridView = root.findViewById(R.id.grid_view);
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // Here, we just assume the integer 'position' is the stress value.
                Toast.makeText(getContext(), Integer.toString(position), Toast.LENGTH_SHORT).show();
                // TODO create a new Activity
                Bundle bundle = new Bundle();
                bundle.putInt("grid_id", gridImgID);
                bundle.putInt("position", position);
                Intent intent = new Intent(getContext(), BigImgActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        GridViewAdapter gridViewAdapter = new GridViewAdapter(root.getContext(), gridImgID);
        gridView.setAdapter(gridViewAdapter);
        return root;
    }

    public static void updateGridImgID() {
        gridImgID++;
        gridImgID %= 3;
    }

    public static int getGridImgID() { return gridImgID; }
}