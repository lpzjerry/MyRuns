package com.dartmouth.cs.myruns2;

import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.TextView;
import android.util.Log;

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

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ServiceConnection {

    private GoogleMap mMap;
    TextView textView;
    public MyMessageHandler myMessageHandler;
    private boolean isBind = false;
    private Intent intent;
    private boolean mapCentered;
    private MarkerOptions redMarkerOption, greenMarkerOption;
    Marker marker = null;
    private PolylineOptions polylineOptions;
    ArrayList<Polyline> polylines;
    private String type = "";
    private double avg_speed = 0.0, cur_speed = 0.0, climb = 0.0, calorie = 0.0, distance = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getString("activityType");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        textView = (TextView) findViewById(R.id.map_info);

        myMessageHandler = new MyMessageHandler();

        intent = new Intent(this, LocationService.class);
        getApplicationContext().startService(intent);
        if (!isBind) {
            //getApplicationContext().bindService(intent, mConnection, 0);
            getApplicationContext().bindService(intent, this, Context.BIND_AUTO_CREATE);
            isBind = true;
        }
        mapCentered = false;
        polylineOptions = new PolylineOptions();
        redMarkerOption = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_RED));
        greenMarkerOption = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_GREEN));
        polylines = new ArrayList<>();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(0, 0);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder iBinder) {
        Log.d("pengze", "Activity: onServiceConnected()");
        LocationService.MyBinder myBinder = (LocationService.MyBinder) iBinder;
        myBinder.setMessageHandler(myMessageHandler);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    @SuppressLint("HandlerLeak")
    public class MyMessageHandler extends Handler {
        @SuppressLint("SetTextI18n")
        public void handleMessage(Message message) {
            Bundle bundle = message.getData();
            double lat = bundle.getDouble(LocationService.LAT_KEY);
            double lng = bundle.getDouble(LocationService.LNG_KEY);
            avg_speed = bundle.getDouble(LocationService.AVG_KEY);
            cur_speed = bundle.getDouble(LocationService.CUR_KEY);
            climb = bundle.getDouble(LocationService.CLIMB_KEY);
            calorie = bundle.getDouble(LocationService.CAL_KEY);
            distance = bundle.getDouble(LocationService.DIST_KEY);
            textView.setText("(" + lat + "," + lng + ")");
            textView.setText(getTextViewContent());
            // TODO Add pin and polyline to the position
            LatLng latLng = new LatLng(lat, lng);

            if(!mapCentered){
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                mMap.animateCamera(cameraUpdate);
                mMap.addMarker(redMarkerOption.position(latLng));
                polylineOptions.add(latLng);
                mapCentered = true;
            } else {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
                mMap.animateCamera(cameraUpdate);
                if (marker != null)
                    marker.remove();
                marker = mMap.addMarker(greenMarkerOption.position(latLng));
                polylineOptions.add(latLng);
                polylines.add(mMap.addPolyline(polylineOptions));
            }

        }
    }

    public void onClickButtonSave(View view) {
        // unbind
        if (isBind) {
            getApplicationContext().unbindService(this);
            getApplicationContext().stopService(intent);
            isBind = false;
        }
        stopService(intent);
        finish();
    }

    public void onClickButtonCancel(View view) {
        // unbind
        if (isBind) {
            getApplicationContext().unbindService(this);
            getApplicationContext().stopService(intent);
            isBind = false;
        }
        stopService(intent);
        finish();
    }

    private String getTextViewContent() {
        String s = "Type: " + type + "\nAvg speed: " + round(avg_speed) + " m/h\nCur speed: ";
        if (cur_speed < 0)
            s += "n/a";
        else
            s += round(cur_speed) + " m/h";
        s += "\nClimb: " + round(climb) + " Miles\nCalorie: "
                + round(calorie) + "\nDistance: " + round(distance) + " Miles";
        return s;
    }

    private static String round(double value) {
        if ((value == Math.floor(value)) && !Double.isInfinite(value)) {
            return Integer.toString((int) value);
        }
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return Double.toString(bd.doubleValue());
    }
}
