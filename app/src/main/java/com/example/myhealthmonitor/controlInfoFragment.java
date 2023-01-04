package com.example.myhealthmonitor;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class controlInfoFragment extends Fragment {
    private View view;
    private DBHelper dbh;
    private String date;
    private boolean repeat;
    private String time;
    private int timing;
    private int reportType;
    private int valueType;
    private float max;
    private float min;
    private int maxAttention;
    private int minAttention;
    private boolean sound;
    private boolean vibration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_control_info, container, false);
        return view;
    }

    @Override
    public void onStart() {
        setPicker();
        setListenerTypeReport();
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
        TextView dateTV = getActivity().findViewById(R.id.controlDate);
        dateTV.setText("");
        
        //onClick on dateTV open DatePicker in a Dialog
        ImageView dateIV = getActivity().findViewById(R.id.controlDatePicker);
        dateIV.setOnClickListener(v -> new DatePickerDialog(
                getContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    SimpleDateFormat dateFormat1 = getDateFormat();
                    dateTV.setText(dateFormat1.format(myCalendar.getTime()));

                    Chip monday = getActivity().findViewById(R.id.controlMonday);
                    monday.setChecked(false);

                    Chip tuesday = getActivity().findViewById(R.id.controlTuesday);
                    tuesday.setChecked(false);

                    Chip wednesday = getActivity().findViewById(R.id.controlWednesday);
                    wednesday.setChecked(false);

                    Chip thursday = getActivity().findViewById(R.id.controlThursday);
                    thursday.setChecked(false);

                    Chip friday = getActivity().findViewById(R.id.controlFriday);
                    friday.setChecked(false);

                    Chip saturday = getActivity().findViewById(R.id.controlSaturday);
                    saturday.setChecked(false);

                    Chip sunday = getActivity().findViewById(R.id.controlSunday);
                    sunday.setChecked(false);
                },
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
        ).show());

        TimePicker picker = getActivity().findViewById(R.id.controlTimePicker);
        picker.setIs24HourView(timeFormatIs24());
    }

    private void setListenerTypeReport(){
        ((Spinner)getActivity().findViewById(R.id.controlTypeReport)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Spinner spinner = getActivity().findViewById(R.id.controlTypeValue);
                ArrayAdapter<CharSequence> adapter;
                switch (((Spinner)getActivity().findViewById(R.id.controlTypeReport)).getSelectedItemPosition()){
                    case 0:
                        adapter = ArrayAdapter.createFromResource(getContext(), R.array.valueGEN, android.R.layout.simple_spinner_item);
                        break;
                    case 1:
                        adapter = ArrayAdapter.createFromResource(getContext(), R.array.valuePA, android.R.layout.simple_spinner_item);
                        break;
                    case 2:
                        adapter = ArrayAdapter.createFromResource(getContext(), R.array.valueG, android.R.layout.simple_spinner_item);
                        break;
                    case 3:
                        adapter = ArrayAdapter.createFromResource(getContext(), R.array.valueW, android.R.layout.simple_spinner_item);
                        break;
                    case 4:
                        adapter = ArrayAdapter.createFromResource(getContext(), R.array.valueP, android.R.layout.simple_spinner_item);
                        break;
                    case 5:
                        adapter = ArrayAdapter.createFromResource(getContext(), R.array.valueO, android.R.layout.simple_spinner_item);
                        break;
                    default:
                        adapter = ArrayAdapter.createFromResource(getContext(), R.array.valueT, android.R.layout.simple_spinner_item);
                        break;
                }
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setListenerChips(){
        Chip monday = getActivity().findViewById(R.id.controlMonday);
        monday.setOnClickListener(v -> {
            TextView dateTV = getActivity().findViewById(R.id.controlDate);
            dateTV.setText("");
        });

        Chip tuesday = getActivity().findViewById(R.id.controlTuesday);
        tuesday.setOnClickListener(v -> {
            TextView dateTV = getActivity().findViewById(R.id.controlDate);
            dateTV.setText("");
        });

        Chip wednesday = getActivity().findViewById(R.id.controlWednesday);
        wednesday.setOnClickListener(v -> {
            TextView dateTV = getActivity().findViewById(R.id.controlDate);
            dateTV.setText("");
        });

        Chip thursday = getActivity().findViewById(R.id.controlThursday);
        thursday.setOnClickListener(v -> {
            TextView dateTV = getActivity().findViewById(R.id.controlDate);
            dateTV.setText("");
        });

        Chip friday = getActivity().findViewById(R.id.controlFriday);
        friday.setOnClickListener(v -> {
            TextView dateTV = getActivity().findViewById(R.id.controlDate);
            dateTV.setText("");
        });

        Chip saturday = getActivity().findViewById(R.id.controlSaturday);
        saturday.setOnClickListener(v -> {
            TextView dateTV = getActivity().findViewById(R.id.controlDate);
            dateTV.setText("");
        });

        Chip sunday = getActivity().findViewById(R.id.controlSunday);
        sunday.setOnClickListener(v -> {
            TextView dateTV = getActivity().findViewById(R.id.controlDate);
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
        boolean code = dbh.deleteControl(idTuple);
        if (code) {
            Control control = new Control(Integer.parseInt(idTuple), date, repeat, time, timing, reportType, valueType, max, min, maxAttention, minAttention, sound, vibration);
            control.delete(getContext());
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
            boolean code = dbh.modifyControl(idTuple, date, repeat, time, timing, reportType, valueType, max, min, maxAttention, minAttention, sound, vibration);
            if (code) {
                Control control = new Control(Integer.parseInt(idTuple), date, repeat, time, timing, reportType, valueType, max, min, maxAttention, minAttention, sound, vibration);
                control.delete(getContext());
                control.schedule(getContext());
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
            long code = dbh.insertNewControl(date, repeat, time, timing, reportType, valueType, max, min, maxAttention, minAttention, sound, vibration);
            if (code>-1) {
                Control control = new Control((int)code, date, repeat, time, timing, reportType, valueType, max, min, maxAttention, minAttention, sound, vibration);
                control.schedule(getContext());
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
            Cursor cursor = dbh.getControl(Integer.toString(idTuple));
            cursor.moveToFirst();

            if (!cursor.isAfterLast()) {

                String time = cursor.getString(cursor.getColumnIndex(DBHelper.C_TIME));
                String[] timeSeparated = time.split(":");
                TimePicker picker = getActivity().findViewById(R.id.controlTimePicker);
                picker.setHour(Integer.parseInt(timeSeparated[0]));
                picker.setMinute(Integer.parseInt(timeSeparated[1]));

                String date = cursor.getString(cursor.getColumnIndex(DBHelper.C_DATE));

                if (cursor.getInt(cursor.getColumnIndex(DBHelper.C_REPEAT))==1){
                    String[] dateSeparated = date.split(";");
                    int i=0;
                    if (dateSeparated[i].equals("0")){
                        Chip monday = getActivity().findViewById(R.id.controlMonday);
                        monday.setChecked(true);
                        i++;
                    }
                    if (i<dateSeparated.length) {
                        if (dateSeparated[i].equals("1")) {
                            Chip tuesday = getActivity().findViewById(R.id.controlTuesday);
                            tuesday.setChecked(true);
                            i++;
                        }
                    }
                    if (i<dateSeparated.length) {
                        if (dateSeparated[i].equals("2")) {
                            Chip wednesday = getActivity().findViewById(R.id.controlWednesday);
                            wednesday.setChecked(true);
                            i++;
                        }
                    }
                    if (i<dateSeparated.length) {
                        if (dateSeparated[i].equals("3")) {
                            Chip thursday = getActivity().findViewById(R.id.controlThursday);
                            thursday.setChecked(true);
                            i++;
                        }
                    }
                    if (i<dateSeparated.length) {
                        if (dateSeparated[i].equals("4")) {
                            Chip friday = getActivity().findViewById(R.id.controlFriday);
                            friday.setChecked(true);
                            i++;
                        }
                    }
                    if (i<dateSeparated.length) {
                        if (dateSeparated[i].equals("5")) {
                            Chip saturday = getActivity().findViewById(R.id.controlSaturday);
                            saturday.setChecked(true);
                            i++;
                        }
                    }
                    if (i<dateSeparated.length) {
                        if (dateSeparated[i].equals("6")) {
                            Chip sunday = getActivity().findViewById(R.id.controlSunday);
                            sunday.setChecked(true);
                        }
                    }
                }
                else{
                    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                    try {
                        Date d = format.parse(date);
                        SimpleDateFormat dateFormat1 = getDateFormat();
                        TextView dateTV = getActivity().findViewById(R.id.controlDate);
                        dateTV.setText(dateFormat1.format(d));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                ((Spinner)getActivity().findViewById(R.id.controlTiming)).setSelection(cursor.getInt(cursor.getColumnIndex(DBHelper.C_TIMING)));

                ((Spinner)getActivity().findViewById(R.id.controlTypeReport)).setSelection(cursor.getInt(cursor.getColumnIndex(DBHelper.C_REPORT_TYPE)));

                ((Spinner)getActivity().findViewById(R.id.controlTypeValue)).setSelection(cursor.getInt(cursor.getColumnIndex(DBHelper.C_VALUE_TYPE)));

                EditText maxET = view.findViewById(R.id.controlMaxValue);
                String max = cursor.getString(cursor.getColumnIndex(DBHelper.C_MAX));
                maxET.setText(max);

                EditText minET = view.findViewById(R.id.controlMinValue);
                String min = cursor.getString(cursor.getColumnIndex(DBHelper.C_MIN));
                minET.setText(min);

                ((Spinner)getActivity().findViewById(R.id.controlMinAttention)).setSelection(cursor.getInt(cursor.getColumnIndex(DBHelper.C_MIN_ATTENTION)));

                ((Spinner)getActivity().findViewById(R.id.controlMaxAttention)).setSelection(cursor.getInt(cursor.getColumnIndex(DBHelper.C_MAX_ATTENTION)));

                /*((SwitchMaterial)getActivity().findViewById(R.id.controlSoundActive)).setChecked(cursor.getInt(cursor.getColumnIndex(DBHelper.C_SOUND)) == 1);

                ((SwitchMaterial)getActivity().findViewById(R.id.controlVibrationActive)).setChecked(cursor.getInt(cursor.getColumnIndex(DBHelper.C_VIBRATION)) == 1);*/
            }
            cursor.close();
        }
    }

    private boolean getData(){
        //get control time
        TimePicker picker = getActivity().findViewById(R.id.controlTimePicker);
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
        TextView dateTV = getActivity().findViewById(R.id.controlDate);
        date = dateTV.getText().toString();
        repeat = false;
        if (!date.equals("")){
            SharedPreferences pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
            if (!pref.getBoolean(MainActivity.DATEDEF,true)) {
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
                try {
                    Date dateD = format.parse(date);
                    System.out.println(dateD);
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
                    System.out.println(dateD);
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
            Chip monday = getActivity().findViewById(R.id.controlMonday);
            if (monday.isChecked()){
                date += "0" + ";";
                repeat=true;
            }
            Chip tuesday = getActivity().findViewById(R.id.controlTuesday);
            if (tuesday.isChecked()){
                date += "1" + ";";
                repeat=true;
            }
            Chip wednesday = getActivity().findViewById(R.id.controlWednesday);
            if (wednesday.isChecked()){
                date += "2" + ";";
                repeat=true;
            }
            Chip thursday = getActivity().findViewById(R.id.controlThursday);
            if (thursday.isChecked()){
                date += "3" + ";";
                repeat=true;
            }
            Chip friday = getActivity().findViewById(R.id.controlFriday);
            if (friday.isChecked()){
                date += "4" + ";";
                repeat=true;
            }
            Chip saturday = getActivity().findViewById(R.id.controlSaturday);
            if (saturday.isChecked()){
                date += "5" + ";";
                repeat=true;
            }
            Chip sunday = getActivity().findViewById(R.id.controlSunday);
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
                System.out.println("time input=> " + timeCal.getTimeInMillis());
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

        timing = ((Spinner)getActivity().findViewById(R.id.controlTiming)).getSelectedItemPosition();
        reportType = ((Spinner)getActivity().findViewById(R.id.controlTypeReport)).getSelectedItemPosition();
        valueType = ((Spinner)getActivity().findViewById(R.id.controlTypeValue)).getSelectedItemPosition();

        EditText maxET = getActivity().findViewById(R.id.controlMaxValue);
        String maxStr = maxET.getText().toString();
        EditText minET = getActivity().findViewById(R.id.controlMinValue);
        String minStr = minET.getText().toString();
        if (maxStr.isEmpty() && minStr.isEmpty()) {
            new AlertDialog.Builder(getContext())
                    .setMessage(getResources().getString(R.string.minMaxErrorNoValue))
                    .setPositiveButton(android.R.string.yes, null)
                    .show();
            return false;
        } else {
            if (maxStr.isEmpty()) {
                max = -23;
            } else {
                max = Float.parseFloat(maxStr);
            }
            if (minStr.isEmpty()) {
                min = -23;
            } else {
                min = Float.parseFloat(minStr);
            }
            if (max<min && max!=-23){
                new AlertDialog.Builder(getContext())
                        .setMessage(getResources().getString(R.string.minMaxError))
                        .setPositiveButton(android.R.string.yes, null)
                        .show();
                return false;
            }
        }

        minAttention = ((Spinner)getActivity().findViewById(R.id.controlMinAttention)).getSelectedItemPosition();
        maxAttention = ((Spinner)getActivity().findViewById(R.id.controlMaxAttention)).getSelectedItemPosition();
        if (minAttention>maxAttention){
            int a=maxAttention;
            maxAttention=minAttention;
            minAttention=a;
        }

        /*sound = ((SwitchMaterial)getActivity().findViewById(R.id.controlSoundActive)).isChecked();
        vibration = ((SwitchMaterial)getActivity().findViewById(R.id.controlVibrationActive)).isChecked();*/
        sound=true;
        vibration=true;


        return true;
    }
}