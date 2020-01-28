package edu.dartmouth.stressmeter.ui.tools;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.view.ViewGroup.LayoutParams;
import android.util.Log;
import android.content.res.AssetManager;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import edu.dartmouth.stressmeter.MainActivity;
import edu.dartmouth.stressmeter.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

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
        AssetManager am = getActivity().getAssets();
        try {
            csvContent = readFromFile(getContext());
            // Edit Table
            TableLayout tableLayout = (TableLayout) root.findViewById(R.id.table);
            int x = 0;
            int y = 0;
            for (String substr : csvContent.split("\n")) {
                String[] elements = substr.split(",");
                TableRow tr_head = new TableRow(getContext());
                tr_head.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                // 1
                TextView tv1 = new TextView(getContext());
                tv1.setBackground(getResources().getDrawable(R.drawable.border_ui));
                tv1.setId(x);
                x++;
                tv1.setText(elements[0]);
                tr_head.addView(tv1);
                // 2
                TextView tv2 = new TextView(getContext());
                tv2.setBackground(getResources().getDrawable(R.drawable.border_ui));
                tv2.setId(y);
                y++;
                tv2.setText(elements[1]);
                tr_head.addView(tv2);
                tableLayout.addView((View) tr_head);
            }
            // Edit Chart
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
        } catch (Exception e) {
            Log.d("pengze", "ERROR LOADING CSV: " + e.toString());
        }
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