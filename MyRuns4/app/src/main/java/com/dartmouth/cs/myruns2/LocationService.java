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

/*
Ref: https://stackoverflow.com/questions/14478179/background-service-with-location-listener-in-android
 */

public class LocationService extends Service {

    public static final String LAT_KEY = "lat_key";
    public static final String LNG_KEY = "lng_key";
    LocationManager locationManager;
    double lat, lng;
    private Location location;
    private MapsActivity.MyMessageHandler locationServiceMsgHandler = null;

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("pengze", "Service: onCreate()");
        lat = 0;
        lng = 0;
        initLocationManager();
        /*String svcName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(svcName);
        myMessageHandler = new MyMessageHandler();
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(true);
        criteria.setSpeedRequired(true);
        String provider = LocationManager.GPS_PROVIDER;
        Log.d("pengze", "provider " + provider); // NULL
        Location location = locationManager.getLastKnownLocation(provider);
        locationManager.requestLocationUpdates(provider, 0, 0, locationListener);*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("pengze", "Service: onDestroy()");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("pengze", "Service: onBind()." + lat + "," + lng);
        return new MyBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("pengze", "Service: onUnbind()" + lat + "," + lng);
        return true; // false: activity can bind only once.
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("pengze", "Service onStartCommand() called: "+startId);
        return START_STICKY; // START_NOT_STICKY, START_REDELIVER_INTENT
    }

    public class MyBinder extends Binder {
        public void setMessageHandler(MapsActivity.MyMessageHandler msgHandler) {
            Log.d("pengze", "Service: setMessageHandler");
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
            Log.d("pengze", "onLocationChanged()");
            if (location != null) {
                lat = location.getLatitude();
                lng = location.getLongitude();
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
        Log.d("pengze", "updateWithNewLocation()");
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            Log.d("pengze", "(" + lat + "," + lng + ")");
            Bundle bundle = new Bundle();
            bundle.putDouble(LAT_KEY, lat);
            bundle.putDouble(LNG_KEY, lng);
            Message message = locationServiceMsgHandler.obtainMessage();
            message.setData(bundle);
            locationServiceMsgHandler.sendMessage(message);
        }
    }

    public static LatLng fromLocationToLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }
}