package com.dartmouth.cs.myruns2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "records.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_RECORDS = "records";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_DISTANCE = "distance";
    public static final String COLUMN_CALORIES = "calories";
    public static final String COLUMN_HEART_RATE = "heartrate";

    public MySQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase database) {
        Log.d("pengze", "onCreate() called");
        String sql = "create table "
                + TABLE_RECORDS + "(" + COLUMN_ID
                + " integer primary key autoincrement, "
                + COLUMN_TYPE + " text not null,"
                + COLUMN_DATE + " text not null,"
                + COLUMN_DURATION + " integer not null,"
                + COLUMN_DISTANCE + " integer not null,"
                + COLUMN_CALORIES + " integer not null,"
                + COLUMN_HEART_RATE + " integer not null);";
        Log.d("pengze", sql);
        database.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.d("pengze", "onUpgrade() called");
    }

}