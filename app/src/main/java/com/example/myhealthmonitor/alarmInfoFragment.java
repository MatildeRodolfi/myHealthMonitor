package com.example.myhealthmonitor;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.fragment.app.Fragment;

public class alarmInfoFragment extends Fragment {
    private View view;
    private DBHelper dbh;
    private String date;
    private boolean repeat;
    private String time;
    private String name;
    private boolean sound;
    private boolean vibration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_alarm_info, container, false);
        return view;
    }

    @Override
    public void onStart() {
        setPicker();
        setListenerChips();
        dbh = MainActivity.getDBHelper();

        upload();
        super.onStart();
    }

    //set Picker (call by fragment)
    private void setPicker(){
        Calendar myCalendar = Calendar.getInstance();
        System.out.println("Current time => " + myCalendar.getTime());

        //set "hint" for date to today
        TextView dateTV = getActivity().findViewById(R.id.alarmDate);
        dateTV.setText("");
        
        //onClick on dateTV open DatePicker in a Dialog
        ImageView dateIV = getActivity().findViewById(R.id.alarmDatePicker);
        dateIV.setOnClickListener(v -> new DatePickerDialog(
                getContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    SimpleDateFormat dateFormat1 = getDateFormat();
                    dateTV.setText(dateFormat1.format(myCalendar.getTime()));

                    Chip monday = getActivity().findViewById(R.id.alarmMonday);
                    monday.setChecked(false);

                    Chip tuesday = getActivity().findViewById(R.id.alarmTuesday);
                    tuesday.setChecked(false);

                    Chip wednesday = getActivity().findViewById(R.id.alarmWednesday);
                    wednesday.setChecked(false);

                    Chip thursday = getActivity().findViewById(R.id.alarmThursday);
                    thursday.setChecked(false);

                    Chip friday = getActivity().findViewById(R.id.alarmFriday);
                    friday.setChecked(false);

                    Chip saturday = getActivity().findViewById(R.id.alarmSaturday);
                    saturday.setChecked(false);

                    Chip sunday = getActivity().findViewById(R.id.alarmSunday);
                    sunday.setChecked(false);
                },
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
        ).show());

        TimePicker picker = getActivity().findViewById(R.id.alarmTimePicker);
        picker.setIs24HourView(timeFormatIs24());
    }

    private void setListenerChips(){
        Chip monday = getActivity().findViewById(R.id.alarmMonday);
        monday.setOnClickListener(v -> {
            TextView dateTV = getActivity().findViewById(R.id.alarmDate);
            dateTV.setText("");
        });

        Chip tuesday = getActivity().findViewById(R.id.alarmTuesday);
        tuesday.setOnClickListener(v -> {
            TextView dateTV = getActivity().findViewById(R.id.alarmDate);
            dateTV.setText("");
        });

        Chip wednesday = getActivity().findViewById(R.id.alarmWednesday);
        wednesday.setOnClickListener(v -> {
            TextView dateTV = getActivity().findViewById(R.id.alarmDate);
            dateTV.setText("");
        });

        Chip thursday = getActivity().findViewById(R.id.alarmThursday);
        thursday.setOnClickListener(v -> {
            TextView dateTV = getActivity().findViewById(R.id.alarmDate);
            dateTV.setText("");
        });

        Chip friday = getActivity().findViewById(R.id.alarmFriday);
        friday.setOnClickListener(v -> {
            TextView dateTV = getActivity().findViewById(R.id.alarmDate);
            dateTV.setText("");
        });

        Chip saturday = getActivity().findViewById(R.id.alarmSaturday);
        saturday.setOnClickListener(v -> {
            TextView dateTV = getActivity().findViewById(R.id.alarmDate);
            dateTV.setText("");
        });

        Chip sunday = getActivity().findViewById(R.id.alarmSunday);
        sunday.setOnClickListener(v -> {
            TextView dateTV = getActivity().findViewById(R.id.alarmDate);
            dateTV.setText("");
        });
    }

    //set date pattern with preference
    private SimpleDateFormat getDateFormat(){
        SharedPreferences pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.getBoolean(MainActivity.DATEDEF,true)) {
            return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        } else{
            return new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        }
    }

    //set date pattern with preference
    private boolean timeFormatIs24(){
        SharedPreferences pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(MainActivity.TIMEDEF, true);
    }
    
    void delete() {
        String idTuple = Integer.toString(getActivity().getIntent().getExtras().getInt("id"));
        boolean code = dbh.deleteAlarm(idTuple);
        if (code) {
            Alarm alarm = new Alarm(Integer.parseInt(idTuple), date, repeat, time, name, sound, vibration);
            alarm.delete(getContext());
            Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.deleted), Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
        else{
            new AlertDialog.Builder(getContext())
                    .setMessage(getResources().getString(R.string.error))
                    .setPositiveButton(android.R.string.yes, null)
                    .show();
        }
    }

    void modify() {
        if (getData()) {
            //save date in db
            String idTuple = Integer.toString(getActivity().getIntent().getExtras().getInt("id"));
            boolean code = dbh.modifyAlarm(idTuple, date, repeat, time, name, sound, vibration);
            if (code) {
                Alarm alarm = new Alarm(Integer.parseInt(idTuple), date, repeat, time, name, sound, vibration);
                alarm.delete(getContext());
                alarm.schedule(getContext());
                Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.saved), Toast.LENGTH_LONG).show();
                getActivity().finish();
            } else {
                new AlertDialog.Builder(getContext())
                        .setMessage(getResources().getString(R.string.error))
                        .setPositiveButton(android.R.string.yes, null)
                        .show();
            }
        }
    }

    void save() {
        if (getData()) {
            //save date in db
            long code = dbh.insertNewAlarm(date, repeat, time, name, sound, vibration);
            if (code>-1) {
                Alarm alarm = new Alarm((int)code, date, repeat, time, name, sound, vibration);
                alarm.schedule(getContext());
                Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.saved), Toast.LENGTH_LONG).show();
                getActivity().finish();
            } else {
                new AlertDialog.Builder(getContext())
                        .setMessage(getResources().getString(R.string.error))
                        .setPositiveButton(android.R.string.yes, null)
                        .show();
            }
        }
    }

    void upload() {
        if (getActivity().getIntent().getExtras()!=null){
            int idTuple = getActivity().getIntent().getExtras().getInt("id");
            Cursor cursor = dbh.getAlarm(Integer.toString(idTuple));
            cursor.moveToFirst();

            if (!cursor.isAfterLast()) {

                String time = cursor.getString(cursor.getColumnIndex(DBHelper.A_TIME));
                String[] timeSeparated = time.split(":");
                TimePicker picker = getActivity().findViewById(R.id.alarmTimePicker);
                picker.setHour(Integer.parseInt(timeSeparated[0]));
                picker.setMinute(Integer.parseInt(timeSeparated[1]));

                String date = cursor.getString(cursor.getColumnIndex(DBHelper.A_DATE));

                if (cursor.getInt(cursor.getColumnIndex(DBHelper.A_REPEAT))==1){
                    String[] dateSeparated = date.split(";");
                    int i=0;
                    if (dateSeparated[i].equals("0")){
                        Chip monday = getActivity().findViewById(R.id.alarmMonday);
                        monday.setChecked(true);
                        i++;
                    }
                    if (i<dateSeparated.length) {
                        if (dateSeparated[i].equals("1")) {
                            Chip tuesday = getActivity().findViewById(R.id.alarmTuesday);
                            tuesday.setChecked(true);
                            i++;
                        }
                    }
                    if (i<dateSeparated.length) {
                        if (dateSeparated[i].equals("2")) {
                            Chip wednesday = getActivity().findViewById(R.id.alarmWednesday);
                            wednesday.setChecked(true);
                            i++;
                        }
                    }
                    if (i<dateSeparated.length) {
                        if (dateSeparated[i].equals("3")) {
                            Chip thursday = getActivity().findViewById(R.id.alarmThursday);
                            thursday.setChecked(true);
                            i++;
                        }
                    }
                    if (i<dateSeparated.length) {
                        if (dateSeparated[i].equals("4")) {
                            Chip friday = getActivity().findViewById(R.id.alarmFriday);
                            friday.setChecked(true);
                            i++;
                        }
                    }
                    if (i<dateSeparated.length) {
                        if (dateSeparated[i].equals("5")) {
                            Chip saturday = getActivity().findViewById(R.id.alarmSaturday);
                            saturday.setChecked(true);
                            i++;
                        }
                    }
                    if (i<dateSeparated.length) {
                        if (dateSeparated[i].equals("6")) {
                            Chip sunday = getActivity().findViewById(R.id.alarmSunday);
                            sunday.setChecked(true);
                        }
                    }
                }
                else{
                    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                    try {
                        Date d = format.parse(date);
                        SimpleDateFormat dateFormat1 = getDateFormat();
                        TextView dateTV = getActivity().findViewById(R.id.alarmDate);
                        dateTV.setText(dateFormat1.format(d));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                EditText nameET = view.findViewById(R.id.alarmName);
                String name = cursor.getString(cursor.getColumnIndex(DBHelper.A_NAME));
                nameET.setText(name);

                ((SwitchMaterial)getActivity().findViewById(R.id.alarmSoundActive)).setChecked(cursor.getInt(cursor.getColumnIndex(DBHelper.A_SOUND)) == 1);

                ((SwitchMaterial)getActivity().findViewById(R.id.alarmVibrationActive)).setChecked(cursor.getInt(cursor.getColumnIndex(DBHelper.A_VIBRATION)) == 1);
            }
            cursor.close();
        }
    }

    private boolean getData(){
        //get control time
        TimePicker picker = getActivity().findViewById(R.id.alarmTimePicker);
        int hour = picker.getHour();
        int minute = picker.getMinute();
        if (minute<10){
            time=hour+":0"+minute;
        }
        else{
            time=hour+":"+minute;
        }
        System.out.println("Time in kk:mm => " + time);

        //get control date
        TextView dateTV = getActivity().findViewById(R.id.alarmDate);
        date = dateTV.getText().toString();
        repeat = false;
        if (!date.equals("")){
            SharedPreferences pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
            if (!pref.getBoolean(MainActivity.DATEDEF,true)) {
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
                try {
                    Date dateD = format.parse(date);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                    date = dateFormat.format(dateD);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            else {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                try {
                    Date dateD = format.parse(date);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                    date = dateFormat.format(dateD);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Date in yyyy/MM/dd => " + date);
        }
        else{
            date="";
            Chip monday = getActivity().findViewById(R.id.alarmMonday);
            if (monday.isChecked()){
                date += "0" + ";";
                repeat=true;
            }
            Chip tuesday = getActivity().findViewById(R.id.alarmTuesday);
            if (tuesday.isChecked()){
                date += "1" + ";";
                repeat=true;
            }
            Chip wednesday = getActivity().findViewById(R.id.alarmWednesday);
            if (wednesday.isChecked()){
                date += "2" + ";";
                repeat=true;
            }
            Chip thursday = getActivity().findViewById(R.id.alarmThursday);
            if (thursday.isChecked()){
                date += "3" + ";";
                repeat=true;
            }
            Chip friday = getActivity().findViewById(R.id.alarmFriday);
            if (friday.isChecked()){
                date += "4" + ";";
                repeat=true;
            }
            Chip saturday = getActivity().findViewById(R.id.alarmSaturday);
            if (saturday.isChecked()){
                date += "5" + ";";
                repeat=true;
            }
            Chip sunday = getActivity().findViewById(R.id.alarmSunday);
            if (sunday.isChecked()){
                date += "6";
                repeat=true;
            }
            if (repeat){
                System.out.println("Repeat weekly in day => " + date);
            }
            else{
                SimpleDateFormat format = new SimpleDateFormat("kk:mm", Locale.getDefault());
                Date timeDate = new Date();
                try {
                    timeDate = format.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar timeCal= Calendar.getInstance();
                timeCal.set(Calendar.HOUR_OF_DAY, timeDate.getHours());
                timeCal.set(Calendar.MINUTE, timeDate.getMinutes());
                System.out.println("time input => " + timeCal.getTimeInMillis());
                System.out.println("time current => " + System.currentTimeMillis());

                Calendar myCalendar = Calendar.getInstance();
                if (timeCal.getTimeInMillis() <= System.currentTimeMillis()){
                    myCalendar.add(Calendar.DAY_OF_YEAR, 1);
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                date = dateFormat.format(myCalendar.getTime());
                System.out.println("Automatically date find => " + date);
            }
        }

        name = ((EditText)getActivity().findViewById(R.id.alarmName)).getText().toString();
        if (name.equals("")){
            name = null;
        }

        sound = ((SwitchMaterial)getActivity().findViewById(R.id.alarmSoundActive)).isChecked();
        vibration = ((SwitchMaterial)getActivity().findViewById(R.id.alarmVibrationActive)).isChecked();

        return true;
    }
}