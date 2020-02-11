package com.dartmouth.cs.myruns2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.List;
import java.util.Locale;
import java.util.TimerTask;
import java.util.Timer;

public class LocationService extends Service {

    public static final String LAT_KEY = "lat_key";
    public static final String LNG_KEY = "lng_key";
    public static final String AVG_KEY = "avg_key";
    public static final String CUR_KEY = "cur_key";
    public static final String CLIMB_KEY = "climb_key";
    public static final String CAL_KEY = "cal_key";
    public static final String DIST_KEY = "dist_key";
    public static final String DUR_KEY = "dur_key";

    LocationManager locationManager;
    private Timer timer;
    private double lat = 0, lng = 0, avg_speed = 0, cur_speed = 0,
            climb = 0, calorie = 0, distance = 0, initAltitude = 0;
    private int sec = 0;
    private Location location;
    private MapsActivity.MyMessageHandler locationServiceMsgHandler = null;

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        CounterTask counterTask = new CounterTask();
        timer.scheduleAtFixedRate(counterTask, 0, 1000);
        initLocationManager();
    }

    public class CounterTask extends TimerTask {
        public void run() {
            sec++;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true; // false: activity can bind only once.
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY; // START_NOT_STICKY, START_REDELIVER_INTENT
    }

    public class MyBinder extends Binder {
        public void setMessageHandler(MapsActivity.MyMessageHandler msgHandler) {
            locationServiceMsgHandler = msgHandler;
            updateWithNewLocation(location);
        }
    }

    private void initLocationManager() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            String provider = locationManager.getBestProvider(criteria, true);
            location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
        } catch (SecurityException e) {
        }
    }


    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (location != null) {
                updateWithNewLocation(location);
            }
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status,
                                    Bundle extras) {
        }
    };

    private void updateWithNewLocation(Location location) {
        if (location != null) {
            if (lat * lng != 0)
                distance += distance(location.getLatitude(), location.getLongitude(), lat, lng);
            lat = location.getLatitude();
            lng = location.getLongitude();
            avg_speed = distance / sec * 3600;
            cur_speed = location.getSpeed();
            if (initAltitude != 0)
                climb = location.getAltitude() - initAltitude;
            else
                climb = 0;
            calorie = distance * 105;
            Log.d("pengze", "(" + lat + "," + lng + ")");
            Bundle bundle = new Bundle();
            bundle.putDouble(LAT_KEY, lat);
            bundle.putDouble(LNG_KEY, lng);
            bundle.putDouble(AVG_KEY, avg_speed);
            bundle.putDouble(CUR_KEY, cur_speed);
            bundle.putDouble(CLIMB_KEY, climb);
            bundle.putDouble(CAL_KEY, calorie);
            bundle.putDouble(DIST_KEY, distance);
            bundle.putInt(DUR_KEY, sec);
            Message message = locationServiceMsgHandler.obtainMessage();
            message.setData(bundle);
            locationServiceMsgHandler.sendMessage(message);
        }
    }

    public static LatLng fromLocationToLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}