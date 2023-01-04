package com.example.myhealthmonitor;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class closeAlarm extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
        getApplicationContext().stopService(intentService);
        stopForeground(true);
        stopSelf();

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
