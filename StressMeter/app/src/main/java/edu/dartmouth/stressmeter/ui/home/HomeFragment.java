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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import edu.dartmouth.stressmeter.GridViewAdapter;
import edu.dartmouth.stressmeter.PSM;
import edu.dartmouth.stressmeter.R;

public class HomeFragment extends Fragment {
    private static int gridImgID = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        GridView gridView = root.findViewById(R.id.grid_view);
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