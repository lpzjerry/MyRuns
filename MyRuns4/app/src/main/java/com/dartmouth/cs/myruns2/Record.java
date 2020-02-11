package com.dartmouth.cs.myruns2;

import android.annotation.SuppressLint;

import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;

public class Record {
    private long id;
    private String type = "Running";
    private String dateAndTime = "";
    private String duration = "";
    private double distance = 0;
    private int calories = 0;
    private String heartRate = "0";

    private static String round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        if ((value == Math.floor(value)) && !Double.isInfinite(value)) {
            return Integer.toString((int) value);
        }
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return Double.toString(bd.doubleValue());
    }

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

    public void setDuration(String s) {
        duration = s;
    }

    public String getDurationStr() {
        return duration;
    }

    public void setDistance(double miles) {
        distance = miles;
    }

    public double getDistance() {
        return distance;
    }

    public String getDistanceStr() {
        int unit = MainActivity.getUnit();
        if (unit == MainActivity.UNIT_MILES) {
            return round(distance, 2) + " Miles";
        }
        return round(distance * MainActivity.KM_TO_MILES, 2) + " Kilometers";
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

    public void setHeartRate(String s) {
        heartRate = s;
    }

    public String getHeartRateStr() {
        return heartRate;
    }
}
