package com.example.myhealthmonitor;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class ControlService extends Service {

    private int timing;
    private int reportType;
    private int valueType;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        timing = intent.getIntExtra("TIMING", 0);
        reportType = intent.getIntExtra("REPORT_TYPE", 0);
        valueType = intent.getIntExtra("VALUE_TYPE", 0);
        float max = intent.getFloatExtra("MAX", -23);
        float min = intent.getFloatExtra("MIN", -23);
        int maxAttention = intent.getIntExtra("MAX_ATTENTION", -23);
        int minAttention = intent.getIntExtra("MIN_ATTENTION", -23);

        float avg = avgValue(maxAttention, minAttention);
        if (avg!=-23) {
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "CONTROL_SERVICE_CHANNEL")
                    .setSmallIcon(R.drawable.ic_people)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
                    .setDefaults(0);
            String tv = "";
            switch (reportType) {
                case 0:
                    tv = getResources().getStringArray(R.array.valueGEN)[valueType];
                    break;
                case 1:
                    tv = getResources().getStringArray(R.array.valuePA)[valueType];
                    break;
                case 2:
                    tv = getResources().getStringArray(R.array.valueG)[valueType];
                    break;
                case 3:
                    tv = getResources().getStringArray(R.array.valueW)[valueType];
                    break;
                case 4:
                    tv = getResources().getStringArray(R.array.valueP)[valueType];
                    break;
                case 5:
                    tv = getResources().getStringArray(R.array.valueO)[valueType];
                    break;
                case 6:
                    tv = getResources().getStringArray(R.array.valueT)[valueType];
                    break;
            }
            if (min>avg || (avg>max && max!=-23)) {
                String s;
                if (min > avg) {
                    s = String.format(getResources().getString(R.string.tooLow), tv, min);
                } else {
                    s = String.format(getResources().getString(R.string.tooHigh), tv, max);
                }
                notification
                        .setContentTitle(getResources().getString(R.string.outTarget))
                        .setContentText(s);

            }
            else{
                String s = String.format(getResources().getString(R.string.inTarget), tv);
                notification
                        .setContentTitle(getResources().getString(R.string.congrats))
                        .setContentText(s);

            }

            // Add as notification
            startForeground(4723, notification.build());
        }

        return START_NOT_STICKY;
    }

    private float avgValue(int minAttention, int maxAttention){
        Calendar day2 = Calendar.getInstance();
        day2.setTimeInMillis(System.currentTimeMillis());

        //find when is the firstDay for the control
        Calendar day1 = Calendar.getInstance();
        day1.setTimeInMillis(System.currentTimeMillis());
        switch (timing){
            case 0: //case Last day
                //firstDay is today => no action
                break;
            case 1: //case Last week
                day1.add(Calendar.DAY_OF_YEAR, -6);
                break;
            case 2: //case Last 15 days
                day1.add(Calendar.DAY_OF_YEAR, -14);
                break;
            case 3: //case Last month
                day1.add(Calendar.DAY_OF_YEAR, -29);
                break;
            case 4: //case Last 6 month
                day1.add(Calendar.DAY_OF_YEAR, -181);
                break;
            case 5: //case Last year
                day1.add(Calendar.DAY_OF_YEAR, -364);
                break;
        }
        String firstDay = day2.get(Calendar.YEAR) +"/";
        String lastDay = day1.get(Calendar.YEAR) +"/";
        if (day2.get(Calendar.MONTH)+1 < 10){
            if (day2.get(Calendar.DAY_OF_MONTH) < 10){
                lastDay += "0"+ (day2.get(Calendar.MONTH) + 1) +"/0"+ (day2.get(Calendar.DAY_OF_MONTH));
            }
            else{
                lastDay += "0"+ (day2.get(Calendar.MONTH) + 1) +"/"+ (day2.get(Calendar.DAY_OF_MONTH));
            }
        }
        else{
            if (day2.get(Calendar.DAY_OF_MONTH) < 10){
                lastDay += (day2.get(Calendar.MONTH) + 1) +"/0"+ (day2.get(Calendar.DAY_OF_MONTH));
            }
            else{
                lastDay += (day2.get(Calendar.MONTH) + 1) +"/"+ (day2.get(Calendar.DAY_OF_MONTH));
            }
        }
        if (day1.get(Calendar.MONTH)+1 < 10){
            if (day1.get(Calendar.DAY_OF_MONTH) < 10){
                firstDay += "0"+ (day1.get(Calendar.MONTH) + 1) +"/0"+ (day1.get(Calendar.DAY_OF_MONTH));
            }
            else{
                firstDay += "0"+ (day1.get(Calendar.MONTH) + 1) +"/"+ (day1.get(Calendar.DAY_OF_MONTH));
            }
        }
        else{
            if (day1.get(Calendar.DAY_OF_MONTH) < 10){
                firstDay += (day1.get(Calendar.MONTH) + 1) +"/0"+ (day1.get(Calendar.DAY_OF_MONTH));
            }
            else{
                firstDay += (day1.get(Calendar.MONTH) + 1) +"/"+ (day1.get(Calendar.DAY_OF_MONTH));
            }
        }

        DBHelper dbh = new DBHelper(getApplicationContext());
        Cursor cursor;
        String column = "";
        boolean isInt = false;

        switch (reportType){
            case 0: //case general
                ArrayList<Cursor> cursorsArray = new ArrayList<>();
                cursorsArray.add(dbh.getPhysicalActivityReportsOverPeriod(firstDay, lastDay));
                cursorsArray.add(dbh.getGlycemiaReportsOverPeriod(firstDay, lastDay));
                cursorsArray.add(dbh.getWeightReportsOverPeriod(firstDay, lastDay));
                cursorsArray.add(dbh.getPulseReportsOverPeriod(firstDay, lastDay));
                cursorsArray.add(dbh.getOxygenReportsOverPeriod(firstDay, lastDay));
                cursorsArray.add(dbh.getTemperatureReportsOverPeriod(firstDay, lastDay));
                int n=0;
                for (int i=0; i<6; i++) {
                    Cursor c = cursorsArray.get(i);
                    c.moveToFirst();
                    while (!c.isAfterLast()) {
                        int a = c.getInt(c.getColumnIndex(DBHelper.T_ATTENTION));
                        if (a>=minAttention && a<=maxAttention) {
                            n++;
                        }
                        c.moveToNext();
                    }
                    c.close();
                }
                return n;
            case 1: //case physicalActivity
                cursor = dbh.getPhysicalActivityReportsOverPeriod(firstDay, lastDay);
                switch (valueType){
                    case 0: //case calories
                        column=DBHelper.PA_CALORIES;
                        isInt=true;
                        break;
                    case 1: //case steps
                        column=DBHelper.PA_STEPS;
                        isInt=true;
                        break;
                    case 2: //case distance
                        column=DBHelper.PA_DISTANCE;
                        isInt=false;
                        break;
                    case 3: //case duration
                        column=DBHelper.PA_DURATION;
                        isInt=true;
                        break;
                }
                break;
            case 2: //case glycemia
                cursor = dbh.getGlycemiaReportsOverPeriod(firstDay, lastDay);
                switch (valueType){
                    case 0: //case glycemiaVal
                        column=DBHelper.G_VALUE;
                        isInt=false;
                        break;
                    case 1: //case HbA1C
                        column=DBHelper.G_HbA1C;
                        isInt=false;
                        break;
                    case 2: //case breadUnit
                        column=DBHelper.G_BREAD_UNIT;
                        isInt=true;
                        break;
                    case 3: //case bolus
                        column=DBHelper.G_BOLUS;
                        isInt=false;
                        break;
                    case 4: //case basal
                        column=DBHelper.G_BASAL;
                        isInt=false;
                        break;
                }
                break;
            case 3: //case weight
                cursor = dbh.getWeightReportsOverPeriod(firstDay, lastDay);
                switch (valueType){
                    case 0: //case weight
                        column=DBHelper.W_VALUE;
                        isInt=false;
                        break;
                    case 1: //case muscles
                        column=DBHelper.W_MUSCLES;
                        isInt=false;
                        break;
                    case 2: //case bodyFat
                        column=DBHelper.W_BODY_FAT;
                        isInt=false;
                        break;
                    case 3: //case water
                        column=DBHelper.W_WATER;
                        isInt=false;
                        break;
                }
                break;
            case 4: //case pulse
                cursor = dbh.getPulseReportsOverPeriod(firstDay, lastDay);
                switch (valueType) {
                    case 0: //case avgPulse
                        column=DBHelper.P_AVG;
                        isInt=true;
                        break;
                    case 1: //case minPulse
                        column=DBHelper.P_MIN;
                        isInt=true;
                        break;
                    case 2: //case maxPulse
                        column=DBHelper.P_MAX;
                        isInt=true;
                        break;
                }
                break;
            case 5: //case oxygenSaturation
                cursor = dbh.getOxygenReportsOverPeriod(firstDay, lastDay);
                switch (valueType) {
                    case 0: //case SpO
                        column=DBHelper.O_AVG;
                        isInt=true;
                        break;
                    case 1: //case SpOMin
                        column=DBHelper.O_MIN;
                        isInt=true;
                        break;
                    case 2: //case SpOMax
                        column=DBHelper.O_MAX;
                        isInt=true;
                        break;
                }
                break;
            default: //case temperature
                cursor = dbh.getTemperatureReportsOverPeriod(firstDay, lastDay);
                column=DBHelper.T_VALUE;
                isInt=false;
                break;
        }

        float sum=0;
        int n=0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int a = cursor.getInt(cursor.getColumnIndex(DBHelper.T_ATTENTION));
            if (a>=minAttention && a<=maxAttention){
                if (isInt){
                    sum += cursor.getInt(cursor.getColumnIndex(column));
                }
                else{
                    sum += cursor.getFloat(cursor.getColumnIndex(column));
                }
                n++;
            }
            cursor.moveToNext();
        }
        cursor.close();
        float r=-23;
        if (n>0 && sum>0){
            r = sum/n;
        }

        return r;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
