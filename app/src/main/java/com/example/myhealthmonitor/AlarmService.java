package com.example.myhealthmonitor;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class AlarmService extends Service {
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        mediaPlayer.setLooping(true);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationCloseIntent = new Intent(this, closeAlarm.class);
        PendingIntent pendingCloseIntent = PendingIntent.getService(this, intent.getIntExtra("ID", -23), notificationCloseIntent, 0);

        String name = intent.getStringExtra("NAME");
        Intent notificationSnoozeIntent = new Intent(this, snoozeAlarm.class);
        notificationSnoozeIntent.putExtra("NAME", name);
        notificationSnoozeIntent.putExtra("SOUND", intent.getBooleanExtra("SOUND", true));
        notificationSnoozeIntent.putExtra("VIBRATION", intent.getBooleanExtra("VIBRATION", true));
        PendingIntent pendingSnoozeIntent = PendingIntent.getService(this, 100000000+intent.getIntExtra("ID", -23), notificationSnoozeIntent, 0);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "ALARM_SERVICE_CHANNEL")
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle(getResources().getString(R.string.alarm))
                .addAction(0, getString(R.string.dismiss), pendingCloseIntent)
                .addAction(0, getString(R.string.plus10), pendingSnoozeIntent)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        if (name!=null){
            notification.setContentTitle(getResources().getString(R.string.alarm)+" - "+name);
        }

        if (intent.getBooleanExtra("SOUND", true)) {
            mediaPlayer.start();
        }

        if (intent.getBooleanExtra("VIBRATION", true)) {
            long[] pattern = {0, 100, 1000};
            vibrator.vibrate(pattern, 0);
        }

        startForeground(4721, notification.build());

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();
        vibrator.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
