package com.dartmouth.cs.myruns2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class RecordDataSource {
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private SQLiteDatabase database;
    private String[] allColumns = new String[]{
            MySQLiteOpenHelper.COLUMN_ID, MySQLiteOpenHelper.COLUMN_TYPE, MySQLiteOpenHelper.COLUMN_DATE,
            MySQLiteOpenHelper.COLUMN_DURATION, MySQLiteOpenHelper.COLUMN_DISTANCE,
            MySQLiteOpenHelper.COLUMN_CALORIES, MySQLiteOpenHelper.COLUMN_HEART_RATE};

    public RecordDataSource(Context context) {
        mySQLiteOpenHelper = MySQLiteOpenHelper.getInstance(context);
    }

    public void open() {
        database = mySQLiteOpenHelper.getWritableDatabase();
    }

    public void close() {
        mySQLiteOpenHelper.close();
    }

    public Record insert(String type, String dateAndTime, int duration, int distance, int calories, int heartRate) {
        Log.d("pengze", "insert(): " + type + " " + dateAndTime);
        ContentValues value = new ContentValues();
        value.put(MySQLiteOpenHelper.COLUMN_TYPE, type);
        value.put(MySQLiteOpenHelper.COLUMN_DATE, dateAndTime);
        value.put(MySQLiteOpenHelper.COLUMN_DURATION, duration);
        value.put(MySQLiteOpenHelper.COLUMN_DISTANCE, distance);
        value.put(MySQLiteOpenHelper.COLUMN_CALORIES, calories);
        value.put(MySQLiteOpenHelper.COLUMN_HEART_RATE, heartRate);
        long id = database.insert(MySQLiteOpenHelper.TABLE_RECORDS, null, value);
        Cursor cursor = database.query(MySQLiteOpenHelper.TABLE_RECORDS, allColumns,
                MySQLiteOpenHelper.COLUMN_ID + "=" + id,
                null, null, null, null);
        cursor.moveToFirst();
        Record ret = cursor2Record(cursor);
        cursor.close();
        return ret;
    }


    private Record cursor2Record(Cursor cursor) {
        Record comment = new Record();
        long recordId = cursor.getLong(0);
        String type = cursor.getString(1);
        String date = cursor.getString(2);
        int duration = cursor.getInt(3);
        int distance = cursor.getInt(4);
        int calories = cursor.getInt(5);
        int heartRate = cursor.getInt(6);
        comment.setId(recordId);
        comment.setType(type);
        comment.setDateAndTime(date);
        comment.setDuration(duration);
        comment.setDistance(distance);
        comment.setCalories(calories);
        comment.setHeartRate(heartRate);
        return comment;
    }

    public void delete(long id) {
        database.delete(MySQLiteOpenHelper.TABLE_RECORDS,
                MySQLiteOpenHelper.COLUMN_ID + "=" + id, null);
    }

    public ArrayList<Record> getAllRecords() {
        ArrayList<Record> comments = new ArrayList<>();
        Cursor cursor = database.query(MySQLiteOpenHelper.TABLE_RECORDS, allColumns,
                null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            comments.add(cursor2Record(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return comments;
    }
}