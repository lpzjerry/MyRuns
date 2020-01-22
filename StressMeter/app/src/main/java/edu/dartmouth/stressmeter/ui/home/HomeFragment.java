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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        // TODO feed images to grids
        GridView gridView = root.findViewById(R.id.grid_view);
        GridViewAdapter gridViewAdapter = new GridViewAdapter(root.getContext());
        gridView.setAdapter(gridViewAdapter);
        return root;
    }
}