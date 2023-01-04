package com.example.myhealthmonitor;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.fragment.app.Fragment;

abstract class ReportFragment extends Fragment {
    //pref variables
    protected SharedPreferences pref;

    //day e time Picker variables
    protected Calendar myCalendar;
    protected EditText dateET;
    protected EditText timeET;
    protected boolean time24;

    //set Pickers (call by fragment)
    protected void setPickers(EditText date, EditText time){
        myCalendar = Calendar.getInstance();
        System.out.println("Current time => " + myCalendar.getTime());

        //set "hint" for date to today
        SimpleDateFormat dateFormat = getDateFormat();
        dateET = date;
        dateET.setText(dateFormat.format(myCalendar.getTime()));

        //set "hint" for time to now
        SimpleDateFormat timeFormat = getTimeFormat();
        timeET = time;
        timeET.setText(timeFormat.format(myCalendar.getTime()));

        //onClick on dateET open DatePicker in a Dialog
        dateET.setOnClickListener(v -> new DatePickerDialog(
                getContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    SimpleDateFormat dateFormat1 = getDateFormat();
                    dateET.setText(dateFormat1.format(myCalendar.getTime()));
                },
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
        ).show());

        //onClick on timeET open TimePicker in a Dialog
        timeET.setOnClickListener(v -> new TimePickerDialog(
                getContext(),
                (timePicker, selectedHour, selectedMinute) -> {
                    myCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                    myCalendar.set(Calendar.MINUTE, selectedMinute);

                    SimpleDateFormat timeFormat1 = getTimeFormat();
                    timeET.setText(timeFormat1.format(myCalendar.getTime()));
                },
                myCalendar.get(Calendar.HOUR_OF_DAY),
                myCalendar.get(Calendar.MINUTE),
                time24
        ).show());
    }

    //set date pattern with preference
    private SimpleDateFormat getDateFormat(){
        pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.getBoolean(MainActivity.DATEDEF,true)) {
            return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        } else{
            return new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        }
    }

    //set date pattern with preference
    private SimpleDateFormat getTimeFormat(){
        pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.getBoolean(MainActivity.TIMEDEF,true)) {
            time24=true;
            return new SimpleDateFormat("kk:mm", Locale.getDefault());
        } else{
            time24=false;
            return new SimpleDateFormat("hh:mm aa", Locale.getDefault());
        }
    }

    //get date in format yyyy/MM/dd
    protected String getDate(){
        String date = ((EditText)getActivity().findViewById(R.id.date)).getText().toString();

        pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
        if (!pref.getBoolean(MainActivity.DATEDEF,true)) {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
            try {
                Date dateD = format.parse(date);
                System.out.println(dateD);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                date = dateFormat.format(dateD);
                return  date;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                Date dateD = format.parse(date);
                System.out.println(dateD);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                date = dateFormat.format(dateD);
                return  date;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //get time in format kk:mm
    protected String getTime(){
        String time = ((EditText)getActivity().findViewById(R.id.time)).getText().toString();

        pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
        if (!pref.getBoolean(MainActivity.TIMEDEF,true)) {
            SimpleDateFormat format = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
            try {
                Date timeD = format.parse(time);
                SimpleDateFormat timeFormat = new SimpleDateFormat("kk:mm", Locale.getDefault());
                time = timeFormat.format(timeD);
                return  time;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else {
            return time;
        }
        return null;
    }

    //visualize Date in preference form
    protected void setDateInCorrectForm(EditText ETdate, String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        try {
            Date d = format.parse(date);
            SimpleDateFormat dateFormat1 = getDateFormat();
            dateET = ETdate;
            dateET.setText(dateFormat1.format(d));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //visualize Time in preference form
    protected void setTimeInCorrectForm(EditText ETtime, String time) {
        SimpleDateFormat format = new SimpleDateFormat("kk:mm", Locale.getDefault());
        try {
            Date t = format.parse(time);
            SimpleDateFormat dateFormat1 = getTimeFormat();
            timeET = ETtime;
            timeET.setText(dateFormat1.format(t));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    abstract void delete();
    abstract void modify();
    abstract void save();
    abstract void upload();
}
