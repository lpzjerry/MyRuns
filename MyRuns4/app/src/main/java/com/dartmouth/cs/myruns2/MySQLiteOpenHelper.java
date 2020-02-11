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

    private static MySQLiteOpenHelper instance = null;

    public static MySQLiteOpenHelper getInstance(Context context) {
        if (instance == null) {
            instance = new MySQLiteOpenHelper(context);
        }
        return instance;
    }

    public MySQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase database) {
        String sql = "create table "
                + TABLE_RECORDS + "(" + COLUMN_ID
                + " integer primary key autoincrement, "
                + COLUMN_TYPE + " text not null,"
                + COLUMN_DATE + " text not null,"
                + COLUMN_DURATION + " integer not null,"
                + COLUMN_DISTANCE + " double not null,"
                + COLUMN_CALORIES + " integer not null,"
                + COLUMN_HEART_RATE + " text not null);";
        database.execSQL(sql);
    }

    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.d("pengze", "onUpgrade() called");
    }

}