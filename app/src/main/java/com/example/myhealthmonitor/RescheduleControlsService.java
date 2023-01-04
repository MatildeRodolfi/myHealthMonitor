package com.example.myhealthmonitor;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class RescheduleControlsService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        DBHelper dbh = MainActivity.getDBHelper();

        Cursor cursor = dbh.getControls();
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(DBHelper.C_ID));
            String date = cursor.getString(cursor.getColumnIndex(DBHelper.C_DATE));
            boolean repeat = cursor.getInt(cursor.getColumnIndex(DBHelper.C_REPEAT)) == 1;
            String time = cursor.getString(cursor.getColumnIndex(DBHelper.C_TIME));
            int timing = cursor.getInt(cursor.getColumnIndex(DBHelper.C_TIMING));
            int reportType = cursor.getInt(cursor.getColumnIndex(DBHelper.C_REPORT_TYPE));
            int valueType = cursor.getInt(cursor.getColumnIndex(DBHelper.C_VALUE_TYPE));
            float min = cursor.getFloat(cursor.getColumnIndex(DBHelper.C_MIN));
            float max = cursor.getFloat(cursor.getColumnIndex(DBHelper.C_MAX));
            int minAttention = cursor.getInt(cursor.getColumnIndex(DBHelper.C_MIN_ATTENTION));
            int maxAttention = cursor.getInt(cursor.getColumnIndex(DBHelper.C_MAX_ATTENTION));
            boolean sound = cursor.getInt(cursor.getColumnIndex(DBHelper.C_SOUND)) == 1;
            boolean vibration = cursor.getInt(cursor.getColumnIndex(DBHelper.C_VIBRATION)) == 1;
            Control control = new Control(id, date, repeat, time, timing, reportType, valueType, min, max, minAttention, maxAttention, sound, vibration);
            control.schedule(getApplicationContext());
            cursor.moveToNext();
        }

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
