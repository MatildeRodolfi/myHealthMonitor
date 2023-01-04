package com.example.myhealthmonitor;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class RescheduleAlarmsService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        DBHelper dbh = MainActivity.getDBHelper();

        Cursor cursor = dbh.getAlarms();
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(DBHelper.A_ID));
            String date = cursor.getString(cursor.getColumnIndex(DBHelper.A_DATE));
            boolean repeat = cursor.getInt(cursor.getColumnIndex(DBHelper.A_REPEAT)) == 1;
            String time = cursor.getString(cursor.getColumnIndex(DBHelper.A_TIME));
            String name = cursor.getString(cursor.getColumnIndex(DBHelper.A_NAME));
            boolean sound = cursor.getInt(cursor.getColumnIndex(DBHelper.A_SOUND)) == 1;
            boolean vibration = cursor.getInt(cursor.getColumnIndex(DBHelper.A_VIBRATION)) == 1;
            Alarm alarm = new Alarm(id, date, repeat, time, name, sound, vibration);
            alarm.schedule(getApplicationContext());
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
