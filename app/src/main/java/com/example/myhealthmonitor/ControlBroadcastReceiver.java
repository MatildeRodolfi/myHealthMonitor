package com.example.myhealthmonitor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;

public class ControlBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            startRescheduleControlService(context);
        } else {
            if (intent.getBooleanExtra("REPEAT", false)) {
                alarmIsToday(context, intent);
            }
            startControlService(context, intent);
        }
    }

    private void alarmIsToday(Context context, Intent intent) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

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
        intentBroadcast.putExtra("TIMING", intent.getIntExtra("TIMING", 0));
        intentBroadcast.putExtra("REPORT_TYPE", intent.getIntExtra("REPORT_TYPE", 0));
        intentBroadcast.putExtra("VALUE_TYPE", intent.getIntExtra("VALUE_TYPE", 0));
        intentBroadcast.putExtra("MAX", intent.getFloatExtra("MAX", -23));
        intentBroadcast.putExtra("MIN", intent.getFloatExtra("MIN", -23));
        intentBroadcast.putExtra("MAX_ATTENTION", intent.getIntExtra("MAX_ATTENTION", -23));
        intentBroadcast.putExtra("MIN_ATTENTION", intent.getIntExtra("MIN_ATTENTION", -23));
        intentBroadcast.putExtra("SOUND", intent.getBooleanExtra("SOUND", true));
        intentBroadcast.putExtra("VIBRATION", intent.getBooleanExtra("VIBRATION", true));

        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, 100000000+intent.getIntExtra("ID", -23), intentBroadcast, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                alarmPendingIntent
        );
    }

    private void startControlService(Context context, Intent intent) {
        Intent intentService = new Intent(context, ControlService.class);
        intentService.putExtra("TIMING", intent.getIntExtra("TIMING", 0));
        intentService.putExtra("REPORT_TYPE", intent.getIntExtra("REPORT_TYPE", 0));
        intentService.putExtra("VALUE_TYPE", intent.getIntExtra("VALUE_TYPE", 0));
        intentService.putExtra("MAX", intent.getFloatExtra("MAX", -23));
        intentService.putExtra("MIN", intent.getFloatExtra("MIN", -23));
        intentService.putExtra("MAX_ATTENTION", intent.getIntExtra("MAX_ATTENTION", -23));
        intentService.putExtra("MIN_ATTENTION", intent.getIntExtra("MIN_ATTENTION", -23));
        intentService.putExtra("SOUND", intent.getBooleanExtra("SOUND", true));
        intentService.putExtra("VIBRATION", intent.getBooleanExtra("VIBRATION", true));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }

    private void startRescheduleControlService(Context context) {
        Intent intentService = new Intent(context, RescheduleControlsService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }


}