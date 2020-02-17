package com.dartmouth.cs.myruns2;

import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import com.meapsoft.FFT;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ServiceConnection, SensorEventListener {

    private boolean isAutomatic = false;
    private RecordDataSource recordDataSource = MainActivity.recordDataSource;
    public static Calendar calendar = Calendar.getInstance();

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
    private int sec = 0;
    ArrayList<LatLng> positions;

    ////
    private MyTask myTask;
    private Timer timer;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private static ArrayBlockingQueue<Double> mAccBuffer;
    private int cnt_stand = 0;
    private int cnt_walk = 0;
    private int cnt_run = 0;
    ////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getString("activityType");
            isAutomatic = bundle.getBoolean("inputType");
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
        positions = new ArrayList<>();

        ////
        mAccBuffer = new ArrayBlockingQueue<Double>(
                Globals.ACCELEROMETER_BUFFER_CAPACITY);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        ArrayList<Attribute> allAttr = new ArrayList<Attribute>();
        DecimalFormat df = new DecimalFormat("0000");
        for (int i = 0; i < Globals.ACCELEROMETER_BLOCK_CAPACITY; i++) {
            allAttr.add(new Attribute(Globals.FEAT_FFT_COEF_LABEL + df.format(i)));
        }
        allAttr.add(new Attribute(Globals.FEAT_MAX_LABEL));

        myTask = new MyTask();
        timer = new Timer();
        timer.scheduleAtFixedRate(myTask, 0, 1000);
        ////
    }

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

    ////
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            double m = Math.sqrt(event.values[0] * event.values[0]
                    + event.values[1] * event.values[1] + event.values[2]
                    * event.values[2]);
            try {
                mAccBuffer.add(m);
            } catch (IllegalStateException e) {
                ArrayBlockingQueue<Double> newBuf = new ArrayBlockingQueue<Double>(
                        mAccBuffer.size() * 2);
                mAccBuffer.drainTo(newBuf);
                mAccBuffer = newBuf;
                mAccBuffer.add(m);
            }
        }
    }
    ////

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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
            positions.add(latLng);
            if (!mapCentered) {
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
        recordDataSource.open();
        String date_time = calendarToString(calendar);
        String mDuration = secToDuration(sec);
        String heart_rate = positionToString();
        if (isAutomatic) {
            if (cnt_stand >= cnt_walk && cnt_stand >= cnt_run)
                type = "Standing";
            else if (cnt_walk >= cnt_stand && cnt_walk >= cnt_run)
                type = "Walking";
            else
                type = "Running";
        }
        recordDataSource.insert(type, date_time, mDuration, distance, calorie, heart_rate);
        recordDataSource.close();
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

    public static String calendarToString(Calendar calendar) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss MMM d yyyy");
        return format.format(calendar.getTime());
    }

    // helper functions
    private String secToDuration(int second) {
        if (second < 60)
            return second + "secs";
        return (second / 60) + "mins " + (second % 60) + "secs";
    }

    private String positionToString() {
        StringBuilder ret = new StringBuilder();
        for (LatLng position : positions) {
            ret.append(position.latitude + "," + position.longitude + ";");
        }
        ret.append("#").append(avg_speed).append("#").append(climb);
        return ret.toString();
    }

    public class MyTask extends TimerTask {
        public void run() {
            // TODO get accelerometer data
            int blockSize = 0;
            FFT fft = new FFT(Globals.ACCELEROMETER_BLOCK_CAPACITY);
            double[] accBlock = new double[Globals.ACCELEROMETER_BLOCK_CAPACITY];
            double[] im = new double[Globals.ACCELEROMETER_BLOCK_CAPACITY];
            double max = Double.MIN_VALUE;
            while (true) {
                try {
                    accBlock[blockSize++] = mAccBuffer.take();
                    if (blockSize == Globals.ACCELEROMETER_BLOCK_CAPACITY) {
                        blockSize = 0;
                        max = .0;
                        for (double val : accBlock) {
                            if (max < val) {
                                max = val;
                            }
                        }
                        fft.fft(accBlock, im);
                        for (int i = 0; i < accBlock.length; i++) {
                            double mag = Math.sqrt(accBlock[i] * accBlock[i] + im[i]
                                    * im[i]);
                            im[i] = .0; // Clear the field
                        }
                        // TODO grab label from classifier
                        double sports_type_label = WekaClassifier.classify(max);
                        if (sports_type_label == 1) {
                            type = "Walking";
                            cnt_walk += 1;
                        } else if (sports_type_label == 2) {
                            type = "Running";
                            cnt_run += 1;
                        } else {
                            type = "Standing";
                            cnt_stand += 1;
                        }
                        // TODO update textView
                        textView.setText(getTextViewContent());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}