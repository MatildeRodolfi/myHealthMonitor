package com.example.myhealthmonitor;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    //pref variables
    public static final String PREF_NAME = "unit_measure";
    public static final String GLYCEMIADEF = "glycemiaDef";
    public static final String WEIGHTDEF = "weightDef";
    public static final String TEMPERATUREDEF = "temperatureDef";
    public static final String LENGTHEDEF = "lengthDef";
    public static final String TIMEDEF = "hoursDef";
    public static final String DATEDEF = "dateDef";
    public static final String DATEDIARY = "dateDiary";
    public static final String DATECHARTSFIRST = "dateChartsFirst";
    public static final String DATECHARTSLAST = "dateChartsLast";
    public static final String TYPECHARTS = "typeChats";

    public static final String PHYSICAL_ACTIVITY = "PA";
    public static final String PA_ATTENTION = "PAattention";
    public static final String PA_STEPS = "steps";
    public static final String PA_DISTANCE = "distance";
    public static final String PA_DURATION = "duration";
    public static final String PA_COMMENT = "PAcomment";

    public static final String GLYCEMIA = "G";
    public static final String G_ATTENTION = "Gattention";
    public static final String G_LABEL = "label";
    public static final String G_HBA1C = "HbA1C";
    public static final String G_BREAD_UNIT = "breadUnit";
    public static final String G_BOLUS = "bolus";
    public static final String G_BASAL = "basal";
    public static final String G_COMMENT = "Gcomment";

    public static final String WEIGHT = "W";
    public static final String W_ATTENTION = "Wattention";
    public static final String W_MUSCLES = "muscles";
    public static final String W_BODY_FAT = "bodyFat";
    public static final String W_WATER = "water";
    public static final String W_COMMENT = "Wcomment";

    public static final String PULSE = "P";
    public static final String P_ATTENTION = "Pattention";
    public static final String P_MAX = "maxPulse";
    public static final String P_MIN = "minPulse";
    public static final String P_COMMENT = "Pcomment";

    public static final String OXYGEN = "O";
    public static final String O_ATTENTION = "Oattention";
    public static final String O_MAX = "SpOMax";
    public static final String O_MIN = "SpOMin";
    public static final String O_COMMENT = "Ocomment";

    public static final String TEMPERATURE = "O";
    public static final String T_ATTENTION = "Tattention";
    public static final String T_COMMENT = "Tcomment";

    //format float
    public static final String FORMAT = "%.2f";

    //db variables
    private static DBHelper dbh;

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //create db
        dbh = new DBHelper(this);

        //set preferences
        pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        if(!pref.contains("initialized")){
            SharedPreferences.Editor editor = pref.edit();

            editor.putBoolean("initialized", true);

            //Set unit_measure pref
            editor.putBoolean(GLYCEMIADEF, true);
            editor.putBoolean(WEIGHTDEF, true);
            editor.putBoolean(TEMPERATUREDEF, true);
            editor.putBoolean(LENGTHEDEF, true);
            editor.putBoolean(TIMEDEF, true);
            editor.putBoolean(DATEDEF, true);

            editor.apply();

            //set up only the first time you open the app or when you reset it
            long code = dbh.insertNewControl("0;1;2;3;4;5;6", true, "18:30", 0, 0, 0, -23, 2, 4, 0, true, true);
            if (code > -1) {
                Control control = new Control((int) code, "0;1;2;3;4;5;6", true, "18:30", 0, 0, 0, -23, 2, 4, 0, true, true);
                control.schedule(getApplicationContext());
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.saved), Toast.LENGTH_LONG).show();
            } else {
                new AlertDialog.Builder(getApplicationContext())
                        .setMessage(getResources().getString(R.string.error))
                        .setPositiveButton(android.R.string.yes, null)
                        .show();
            }

            Calendar myCalendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
            System.out.println("Current time => " + myCalendar.getTime());
            editor.putString(DATEDIARY, dateFormat.format(myCalendar.getTime()));
            editor.putString(DATECHARTSLAST, dateFormat.format(myCalendar.getTime()));
            myCalendar.add(Calendar.DATE, -30);
            editor.putString(DATECHARTSFIRST, dateFormat.format(myCalendar.getTime()));
            editor.putString(TYPECHARTS, "GEN");
            editor.apply();
        }

        //create channel for alarm and control notifications
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    "ALARM_SERVICE_CHANNEL",
                    "Alarm Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);

            NotificationChannel serviceChannel2 = new NotificationChannel(
                    "CONTROL_SERVICE_CHANNEL",
                    "Control Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager2 = getSystemService(NotificationManager.class);
            manager2.createNotificationChannel(serviceChannel2);
        }

        //close service if application was open with a click on notification
        Intent intentService = new Intent(getApplicationContext(), ControlService.class);
        getApplicationContext().stopService(intentService);

        //set begin frame
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_frame, new com.example.myhealthmonitor.diaryFragment()).commit();
        }

        //bottom_navigation_menu
        final int diaryID = R.id.diary;
        final int chartID = R.id.chart;
        final int alarmID = R.id.alarm;
        final int controlParametersID = R.id.control_parameters;
        final int settingsID = R.id.settings;

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case diaryID:
                    diaryFragment dF = (diaryFragment)getSupportFragmentManager().findFragmentByTag("Diary");
                    if (dF == null || !dF.isVisible()) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_frame, new diaryFragment(), "Diary").commit();
                    }
                    break;
                case chartID:
                    chartFragment cF = (chartFragment)getSupportFragmentManager().findFragmentByTag("Chart");
                    if (cF == null || !cF.isVisible()) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_frame, new chartFragment(), "Chart").commit();
                    }
                    break;
                case alarmID:
                    alarmsFragment aF = (alarmsFragment)getSupportFragmentManager().findFragmentByTag("Alarm");
                    if (aF == null || !aF.isVisible()) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_frame, new alarmsFragment(), "Alarm").commit();
                    }
                    break;
                case controlParametersID:
                    controlsFragment cPF = (controlsFragment)getSupportFragmentManager().findFragmentByTag("Controls");
                    if (cPF == null || !cPF.isVisible()) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_frame, new controlsFragment(), "Controls").commit();
                    }
                    break;
                case settingsID:
                    optionFragment oF = (optionFragment)getSupportFragmentManager().findFragmentByTag("Option");
                    if (oF == null || !oF.isVisible()) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_frame, new optionFragment(), "Option").commit();
                    }
                    break;
            }
            return true;
        });

        //session info for diary and chart (reset every time the application is close but not when is rotate)
        if(getLastNonConfigurationInstance() == null) {
            SharedPreferences.Editor editor = pref.edit();
            Calendar myCalendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
            System.out.println("Current time => " + myCalendar.getTime());
            editor.putString(DATEDIARY, dateFormat.format(myCalendar.getTime()));
            editor.putString(DATECHARTSLAST, dateFormat.format(myCalendar.getTime()));
            myCalendar.add(Calendar.DATE, -30);
            editor.putString(DATECHARTSFIRST, dateFormat.format(myCalendar.getTime()));
            editor.putString(TYPECHARTS, "GEN");

            //Set pref visualization of data for diary
            editor.putBoolean(MainActivity.PHYSICAL_ACTIVITY, true);
            editor.putBoolean(MainActivity.PA_ATTENTION, true);
            editor.putBoolean(MainActivity.PA_STEPS, true);
            editor.putBoolean(MainActivity.PA_DISTANCE, true);
            editor.putBoolean(MainActivity.PA_DURATION, true);
            editor.putBoolean(MainActivity.PA_COMMENT, true);
            editor.putBoolean(MainActivity.GLYCEMIA, true);
            editor.putBoolean(MainActivity.G_ATTENTION, true);
            editor.putBoolean(MainActivity.G_LABEL, true);
            editor.putBoolean(MainActivity.G_HBA1C, true);
            editor.putBoolean(MainActivity.G_BREAD_UNIT, true);
            editor.putBoolean(MainActivity.G_BOLUS, true);
            editor.putBoolean(MainActivity.G_BASAL, true);
            editor.putBoolean(MainActivity.G_COMMENT, true);
            editor.putBoolean(MainActivity.WEIGHT, true);
            editor.putBoolean(MainActivity.W_ATTENTION, true);
            editor.putBoolean(MainActivity.W_MUSCLES, true);
            editor.putBoolean(MainActivity.W_BODY_FAT, true);
            editor.putBoolean(MainActivity.W_WATER, true);
            editor.putBoolean(MainActivity.W_COMMENT, true);
            editor.putBoolean(MainActivity.PULSE, true);
            editor.putBoolean(MainActivity.P_ATTENTION, true);
            editor.putBoolean(MainActivity.P_MAX, true);
            editor.putBoolean(MainActivity.P_MIN, true);
            editor.putBoolean(MainActivity.P_COMMENT, true);
            editor.putBoolean(MainActivity.OXYGEN, true);
            editor.putBoolean(MainActivity.O_ATTENTION, true);
            editor.putBoolean(MainActivity.O_MAX, true);
            editor.putBoolean(MainActivity.O_MIN, true);
            editor.putBoolean(MainActivity.O_COMMENT, true);
            editor.putBoolean(MainActivity.TEMPERATURE, true);
            editor.putBoolean(MainActivity.T_ATTENTION, true);
            editor.putBoolean(MainActivity.T_COMMENT, true);
            editor.apply();
        }
    }

    public static DBHelper getDBHelper() {
        return dbh;
    }
}