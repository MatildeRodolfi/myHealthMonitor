package com.example.myhealthmonitor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;

import androidx.annotation.Nullable;

public class snoozeAlarm extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, 10);//TODO set to 1 here

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int id = new Random().nextInt(Integer.MAX_VALUE);


        Intent intentBroadcast = new Intent(this, AlarmBroadcastReceiver.class);
        intentBroadcast.putExtra("ID", id);
        intentBroadcast.putExtra("REPEAT", false);
        intentBroadcast.putExtra("DATE", "");
        intentBroadcast.putExtra("NAME", intent.getStringExtra("NAME"));
        intentBroadcast.putExtra("SOUND", intent.getBooleanExtra("SOUND", true));
        intentBroadcast.putExtra("VIBRATION", intent.getBooleanExtra("VIBRATION", true));

        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(this, 100000000+id, intentBroadcast, 0);

        alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                alarmPendingIntent
        );

        Toast.makeText(getApplicationContext(), "Snooze 10 min "+intent.getStringExtra("NAME"), Toast.LENGTH_LONG).show();

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
