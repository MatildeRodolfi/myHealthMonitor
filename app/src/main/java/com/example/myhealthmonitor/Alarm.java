package com.example.myhealthmonitor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;

public class Alarm {
    private int id;
    private String date;
    private boolean repeat;
    private String time;
    private String name;
    private boolean sound;
    private boolean vibration;

    public Alarm(int id, String date, boolean repeat, String time, String name, boolean sound, boolean vibration) {
        this.id = id;
        this.date = date;
        this.repeat = repeat;
        this.time = time;
        this.name = name;
        this.sound = sound;
        this.vibration = vibration;
    }

    public int getId() {
        return id;
    }
    public void setDate(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getRepeat() {
        return repeat;
    }
    public void setRepeat(Boolean repeat) {
        this.repeat = repeat;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean getSound() {
        return sound;
    }
    public void setSound(boolean sound) {
        this.sound = sound;
    }

    public boolean getVibration() {
        return vibration;
    }
    public void setVibration(boolean vibration) {
        this.vibration = vibration;
    }

    public void schedule(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.putExtra("ID", id);
        intent.putExtra("REPEAT", repeat);
        intent.putExtra("DATE", date);
        intent.putExtra("NAME", name);
        intent.putExtra("SOUND", sound);
        intent.putExtra("VIBRATION", vibration);

        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, 100000000+id, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        System.out.println("Today: "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+" "+calendar.get(Calendar.YEAR)+"/"+calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.DAY_OF_MONTH));

        String[] timeSeparated = time.split(":");
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeSeparated[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeSeparated[1]));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (!repeat) {
            String[] dateSeparated = date.split("/");
            calendar.set(Calendar.YEAR, Integer.parseInt(dateSeparated[0]));
            calendar.set(Calendar.MONTH, Integer.parseInt(dateSeparated[1])-1);
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateSeparated[2]));
        }
        else{
            boolean r = false;
            int today = calendar.get(Calendar.DAY_OF_WEEK);

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
            while (!r) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                today = calendar.get(Calendar.DAY_OF_WEEK);

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
        }

        // if alarm time has already passed, don't create alarm
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            return;
        }

        System.out.println("Day of the alarm: "+calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)+" "+calendar.get(Calendar.YEAR)+"/"+calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.DAY_OF_MONTH));

        alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    alarmPendingIntent
        );
    }

    public void delete(Context context) {
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.putExtra("REPEAT", repeat);
        intent.putExtra("DATE", date);
        intent.putExtra("NAME", name);
        intent.putExtra("SOUND", sound);
        intent.putExtra("VIBRATION", vibration);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, 100000000+id, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(alarmPendingIntent);
    }
}
