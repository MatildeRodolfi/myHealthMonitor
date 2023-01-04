package com.example.myhealthmonitor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            startRescheduleAlarmsService(context);
        } else {
            if (intent.getBooleanExtra("REPEAT", false)) {
                alarmIsToday(context, intent);
            }
            startAlarmService(context, intent);
        }
    }

    private void alarmIsToday(Context context, Intent intent) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, 1);

        String date = intent.getStringExtra("DATE");

        boolean r = false;

        while (!r) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            int today = calendar.get(Calendar.DAY_OF_WEEK);
            assert date != null;
            switch (today) {
                case Calendar.SUNDAY:
                    r = date.contains("6");
                    break;
                case Calendar.MONDAY:
                    r = date.contains("0");
                    break;
                case Calendar.TUESDAY:
                    r = date.contains("1");
                    break;
                case Calendar.WEDNESDAY:
                    r = date.contains("2");
                    break;
                case Calendar.THURSDAY:
                    r = date.contains("3");
                    break;
                case Calendar.FRIDAY:
                    r = date.contains("4");
                    break;
                case Calendar.SATURDAY:
                    r = date.contains("5");
                    break;
            }
        }
        Intent intentBroadcast = new Intent(context, AlarmBroadcastReceiver.class);
        intentBroadcast.putExtra("ID", intent.getIntExtra("ID", -23));
        intentBroadcast.putExtra("REPEAT", true);
        intentBroadcast.putExtra("DATE", date);
        intentBroadcast.putExtra("NAME", intent.getStringExtra("NAME"));
        intentBroadcast.putExtra("SOUND", intent.getBooleanExtra("SOUND", true));
        intentBroadcast.putExtra("VIBRATION", intent.getBooleanExtra("VIBRATION", true));

        PendingIntent alarmPendingIntentDelete = PendingIntent.getBroadcast(context, 100000000+intent.getIntExtra("ID", -23), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(alarmPendingIntentDelete);

        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, 100000000+intent.getIntExtra("ID", -23), intentBroadcast, 0);
        alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                alarmPendingIntent
        );
    }

    private void startAlarmService(Context context, Intent intent) {
        Intent intentService = new Intent(context, AlarmService.class);
        intentService.putExtra("ID", intent.getIntExtra("ID", -23));
        intentService.putExtra("NAME", intent.getStringExtra("NAME"));
        intentService.putExtra("SOUND", intent.getBooleanExtra("SOUND", true));
        intentService.putExtra("VIBRATION", intent.getBooleanExtra("VIBRATION", true));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }

    private void startRescheduleAlarmsService(Context context) {
        Intent intentService = new Intent(context, RescheduleAlarmsService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }
}