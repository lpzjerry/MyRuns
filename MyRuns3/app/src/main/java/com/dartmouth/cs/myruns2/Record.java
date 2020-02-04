package com.dartmouth.cs.myruns2;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;

public class Record {
    private long id;
    private String type = "Running";
    private String dateAndTime = "";
    private int duration = 0;
    private int distance = 0;
    private int calories = 0;
    private int heartRate = 0;

    public void setId(long ID) {
        id = ID;
    }

    public long getId() {
        return id;
    }

    public void setType(String s) {
        type = s;
    }

    public String getType() {
        return type;
    }

    public void setDateAndTime(String calendar) {
        dateAndTime = calendar;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDuration(int i) {
        duration = i;
    }

    public int getDuration() {
        return duration;
    }

    public String getDurationStr() {
        if (duration == 0) {
            return "0secs";
        }
        return duration + "mins 0secs";
    }

    public void setDistance(int i) {
        distance = i;
    }

    public int getDistance() {
        return distance;
    }

    public String getDistanceStr() {
        int unit = MainActivity.getUnit();
        if (unit == MainActivity.UNIT_MILES)
            return distance + " Miles";
        return distance * 1.60934 + " Kilometers";
    }

    public void setCalories(int i) {
        calories = i;
    }

    public int getCalories() {
        return calories;
    }

    public String getCaloriesStr() {
        return calories + " cals";
    }

    public void setHeartRate(int i) {
        heartRate = i;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public String getHeartRateStr() {
        return heartRate + " bpm";
    }
}
