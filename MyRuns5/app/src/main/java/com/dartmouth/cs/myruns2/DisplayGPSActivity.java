package com.dartmouth.cs.myruns2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class DisplayGPSActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private long id;
    private RecordDataSource recordDataSource = MainActivity.recordDataSource;
    private String mInputType = "", mActivityType = "", mDateAndTime = "",
            mDuration = "", mDistance = "", mCalories = "", mHeartRate = "";
    private double avg_speed = 0.0, climb = 0.0;
    private TextView textView;
    private ArrayList<LatLng> positions = new ArrayList<>();


    private MarkerOptions redMarkerOption, greenMarkerOption;
    Marker marker = null;
    private PolylineOptions polylineOptions;
    ArrayList<Polyline> polylines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_gps);
        textView = findViewById(R.id.map_history_info);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        polylineOptions = new PolylineOptions();
        redMarkerOption = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_RED));
        greenMarkerOption = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_GREEN));
        polylines = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getLong("ID");
            mActivityType = bundle.getString("activityType");
            mDateAndTime = bundle.getString("dateAndTime");
            mDuration = bundle.getString("duration");
            mDistance = bundle.getString("distance");
            mCalories = bundle.getString("calories");
            mHeartRate = bundle.getString("heartRate");
            avg_speed = getAvgSpeed(mHeartRate);
            climb = getClimb(mHeartRate);
            String tvText = "Type: " + mActivityType + "\nAvg speed: " + Record.round(avg_speed, 2)
                    + " m/h\nCur speed: n/a\nClimb: " + Record.round(climb, 2) + " Miles\nCalorie: "
                    + mCalories + "\nDistance: " + mDistance;
            textView.setText(tvText);
            // TODO add markers and polylines
            positions = getPositions(mHeartRate);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (positions.size() > 0) {
            LatLng latLng = positions.get(0);
            positions.remove(0);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
            mMap.animateCamera(cameraUpdate);
            mMap.addMarker(redMarkerOption.position(latLng));
            polylineOptions.add(latLng);
        }
        while (positions.size() > 0) {
            LatLng latLng = positions.get(0);
            positions.remove(0);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
            mMap.animateCamera(cameraUpdate);
            if (marker != null)
                marker.remove();
            marker = mMap.addMarker(greenMarkerOption.position(latLng));
            polylineOptions.add(latLng);
            polylines.add(mMap.addPolyline(polylineOptions));
        }
        // LatLng sydney = new LatLng(-34, 151);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_entry, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onClickDeleteEntry(MenuItem item) {
        // TODO delete the entry
        recordDataSource.open();
        recordDataSource.delete(id);
        recordDataSource.close();
        this.finish();
    }

    private ArrayList<LatLng> getPositions(String combo) {
        ArrayList<LatLng> ret = new ArrayList<>();
        String all_pos = combo.split("#")[0];
        if (all_pos.length() > 0) {
            String[] pos_list = all_pos.split(";");
            for (int i = 0; i < pos_list.length - 1; i++) {
                String pos_str = pos_list[i];
                String[] latlngs = pos_str.split(",");
                double lat = Double.parseDouble(latlngs[0]);
                double lng = Double.parseDouble(latlngs[1]);
                ret.add(new LatLng(lat, lng));
            }
        }
        return ret;
    }

    private double getAvgSpeed(String combo) {
        String[] elms = combo.split("#");
        return Double.parseDouble(elms[1]);
    }

    private double getClimb(String combo) {
        String[] elms = combo.split("#");
        return Double.parseDouble(elms[2]);
    }
}