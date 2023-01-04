package com.example.myhealthmonitor;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import static java.lang.String.format;


public class temperatureFragment extends ReportFragment {

    private View view;
    private DBHelper dbh;
    private String date;
    private String time;
    private int attention;
    private float value;
    private String comment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_temperature, container, false);
        return view;
    }

    @Override
    public void onStart() {
        setPickers(getActivity().findViewById(R.id.date), getActivity().findViewById(R.id.time));

        pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.getBoolean(MainActivity.TEMPERATUREDEF,true)) {
            ((TextView) getActivity().findViewById(R.id.temperatureMU)).setText(getResources().getString(R.string.temperature1));
        } else{
            ((TextView) getActivity().findViewById(R.id.temperatureMU)).setText(getResources().getString(R.string.temperature2));
        }

        dbh = MainActivity.getDBHelper();

        upload();
        super.onStart();
    }

    @Override
    void delete() {
        String idTuple = Integer.toString(getActivity().getIntent().getExtras().getInt("id"));
        boolean code = dbh.deleteTemperatureReport(idTuple);
        if (code) {
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

    @Override
    void modify() {
        if (getData()) {
            //save Report date in db
            String idTuple = Integer.toString(getActivity().getIntent().getExtras().getInt("id"));
            boolean code = dbh.modifyTemperatureReport(idTuple, date, time, attention, value, comment);
            if (code) {
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

    @Override
    void save() {
        if (getData()) {
            //save Report date in db
            boolean code = dbh.insertNewTemperatureReport(date, time, attention, value, comment);
            if (code) {
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

    @Override
    void upload() {
        int idTuple = getActivity().getIntent().getExtras().getInt("id");
        if (idTuple>0){
            Cursor cursor = dbh.getTemperatureReport(Integer.toString(idTuple));
            cursor.moveToFirst();

            if (!cursor.isAfterLast()) {

                setDateInCorrectForm(view.findViewById(R.id.date), cursor.getString(cursor.getColumnIndex(DBHelper.T_DATE)));
                setTimeInCorrectForm(view.findViewById(R.id.time), cursor.getString(cursor.getColumnIndex(DBHelper.T_TIME)));

                ((Spinner)getActivity().findViewById(R.id.attention)).setSelection(cursor.getInt(cursor.getColumnIndex(DBHelper.G_ATTENTION)));

                EditText valueET = view.findViewById(R.id.temperatureVal);
                String value = getTemperatureInCorrectForm(cursor.getString(cursor.getColumnIndex(DBHelper.T_VALUE)));
                valueET.setText(value);

                EditText commET = view.findViewById(R.id.comment);
                String comm = cursor.getString(cursor.getColumnIndex(DBHelper.T_COMMENT));
                commET.setText(comm);
            }
            cursor.close();
        }
    }

    //visualize temperature on Pref unit of measure
    private String getTemperatureInCorrectForm(String temperature) {
        if (temperature==null){
            return null;
        }
        pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
        if (!pref.getBoolean(MainActivity.TEMPERATUREDEF, true)) {
            float t;
            t = Float.parseFloat(temperature);
            t = (float) (t * 1.8) + 32;
            return format(Locale.US, MainActivity.FORMAT, t);
        } else {
            return format(Locale.US, MainActivity.FORMAT, Float.parseFloat(temperature));
        }
    }

    private boolean getData(){
        //get Report date
        date = getDate();
        System.out.println("Date in gg/mm/aaaa => " + date);

        time = getTime();
        System.out.println("Time in kk:mm => " + time);

        attention = ((Spinner)getActivity().findViewById(R.id.attention)).getSelectedItemPosition();

        EditText valueET = getActivity().findViewById(R.id.temperatureVal);
        String valueStr = (valueET).getText().toString();
        if (valueStr.trim().isEmpty()) {
            valueET.setError(getText(R.string.required));
            valueET.requestFocus();
            return false;
        } else {
            value = Float.parseFloat(valueStr);
            if (value<=0){
                valueET.setError(getText(R.string.noPositive));
                valueET.requestFocus();
                return false;
            }
            pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
            if (!pref.getBoolean(MainActivity.TEMPERATUREDEF,true)) {
                value = (float) ((value - 32) / 1.8);
            }
            System.out.println("Temperature value in °C => " + value);
        }

        comment = ((EditText)getActivity().findViewById(R.id.comment)).getText().toString();
        if (comment.equals("")){
            comment = null;
        }

        return true;
    }
}