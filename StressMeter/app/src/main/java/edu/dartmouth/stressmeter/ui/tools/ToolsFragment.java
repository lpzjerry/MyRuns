package edu.dartmouth.stressmeter.ui.tools;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;
import android.content.res.AssetManager;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import edu.dartmouth.stressmeter.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;

public class ToolsFragment extends Fragment {
    private String filename = "stress_timestamp.csv";
    private String csvContent = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tools, container, false);
        TextView textView = root.findViewById(R.id.csv_display);
        AssetManager am = getActivity().getAssets();
        csvContent = readFromFile(getContext());
        Log.d("pengze", csvContent);
        textView.setText(csvContent);

        // TODO Edit Chart
        LineChartView chart = (LineChartView) root.findViewById(R.id.chart);
        ArrayList<PointValue> values = new ArrayList<>();
        String[] dataPoints = csvContent.split("\n");
        ArrayList<AxisValue> axisXValues = new ArrayList<AxisValue>();
        ArrayList<AxisValue> axisYValues = new ArrayList<AxisValue>();
        for (int i = 0; i < 16; i++) {
            axisYValues.add(new AxisValue(i));
        }
        int cnt = 0;
        for (String point : dataPoints) {
            int pressure = Integer.parseInt(point.split(",")[1]);
            values.add(new PointValue(cnt, pressure));
            axisXValues.add(new AxisValue(cnt));
            cnt++;
        }
        //In most cased you can call data model methods in builder-pattern-like manner.
        Line line = new Line(values).setColor(Color.BLUE).setCubic(true);
        ArrayList<Line> lines = new ArrayList<Line>();
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);
        Axis axisX = new Axis(axisXValues);
        Axis axisY = new Axis(axisYValues);
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        chart.setLineChartData(data);
        return root;
    }

    private String readFromFile(Context context) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput(filename);
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString).append("\n");
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (Exception e) {
            Log.d("pengze", "File read failed: " + e.toString());
        }
        return ret;
    }
}