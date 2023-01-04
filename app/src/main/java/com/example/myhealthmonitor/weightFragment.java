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

public class weightFragment extends ReportFragment {

    private View view;
    private DBHelper dbh;
    private String date;
    private String time;
    private int attention;
    private float value;
    private float muscles;
    private float bodyFat;
    private float water;
    private String comment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_weight, container, false);
        return view;
    }

    @Override
    public void onStart() {
        setPickers(getActivity().findViewById(R.id.date), getActivity().findViewById(R.id.time));

        pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.getBoolean(MainActivity.WEIGHTDEF,true)) {
            ((TextView) getActivity().findViewById(R.id.weightMU)).setText(getResources().getString(R.string.weight1));
        } else{
            ((TextView) getActivity().findViewById(R.id.weightMU)).setText(getResources().getString(R.string.weight2));
        }

        dbh = MainActivity.getDBHelper();

        upload();
        super.onStart();
    }

    @Override
    void delete() {
        String idTuple = Integer.toString(getActivity().getIntent().getExtras().getInt("id"));
        boolean code = dbh.deleteWeightReport(idTuple);
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
            boolean code = dbh.modifyWeightReport(idTuple, date, time, attention, value, muscles, bodyFat, water, comment);
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
            boolean code = dbh.insertNewWeightReport(date, time, attention, value, muscles, bodyFat, water, comment);
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
            Cursor cursor = dbh.getWeightReport(Integer.toString(idTuple));
            cursor.moveToFirst();

            if (!cursor.isAfterLast()) {

                setDateInCorrectForm(view.findViewById(R.id.date), cursor.getString(cursor.getColumnIndex(DBHelper.W_DATE)));
                setTimeInCorrectForm(view.findViewById(R.id.time), cursor.getString(cursor.getColumnIndex(DBHelper.W_TIME)));

                ((Spinner)getActivity().findViewById(R.id.attention)).setSelection(cursor.getInt(cursor.getColumnIndex(DBHelper.G_ATTENTION)));

                EditText valueET = view.findViewById(R.id.weightVal);
                String value = getWeightInCorrectForm(cursor.getString(cursor.getColumnIndex(DBHelper.W_VALUE)));
                valueET.setText(value);

                EditText musclesET = view.findViewById(R.id.muscles);
                String muscles = cursor.getString(cursor.getColumnIndex(DBHelper.W_MUSCLES));
                musclesET.setText(muscles);

                EditText bodyFatET = view.findViewById(R.id.bodyFat);
                String bodyFat = cursor.getString(cursor.getColumnIndex(DBHelper.W_BODY_FAT));
                bodyFatET.setText(bodyFat);

                EditText waterET = view.findViewById(R.id.water);
                String water = cursor.getString(cursor.getColumnIndex(DBHelper.W_WATER));
                waterET.setText(water);

                EditText commET = view.findViewById(R.id.comment);
                String comm = cursor.getString(cursor.getColumnIndex(DBHelper.W_COMMENT));
                commET.setText(comm);
            }
            cursor.close();
        }
    }

    //visualize weight on Pref unit of measure
    private String getWeightInCorrectForm(String weight) {
        if (weight==null){
            return null;
        }
        pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
        if (!pref.getBoolean(MainActivity.WEIGHTDEF, true)) {
            float w;
            w = Float.parseFloat(weight);
            w = (float) (w * 2.2046);
            return format(Locale.US, MainActivity.FORMAT, w);
        } else {
            return format(Locale.US, MainActivity.FORMAT, Float.parseFloat(weight));
        }
    }

    private boolean getData(){
        //get Report date
        date = getDate();
        System.out.println("Date in gg/mm/aaaa => " + date);

        time = getTime();
        System.out.println("Time in kk:mm => " + time);

        attention = ((Spinner)getActivity().findViewById(R.id.attention)).getSelectedItemPosition();

        EditText valueET = getActivity().findViewById(R.id.weightVal);
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
            if (!pref.getBoolean(MainActivity.WEIGHTDEF,true)) {
                value = (float) (value / 2.2046);
            }
            System.out.println("Weight value in kg => " + value);
        }

        EditText musclesET = getActivity().findViewById(R.id.muscles);
        String musclesStr = musclesET.getText().toString();
        if (musclesStr.trim().isEmpty()) {
            muscles=-23;
        } else {
            muscles = Float.parseFloat(musclesStr);
            if (muscles<=0){
                musclesET.setError(getText(R.string.noPositive));
                musclesET.requestFocus();
                return false;
            }
        }

        EditText bodyFatET = getActivity().findViewById(R.id.bodyFat);
        String bodyFatStr = bodyFatET.getText().toString();
        if (bodyFatStr.trim().isEmpty()) {
            bodyFat=-23;
        } else {
            bodyFat = Float.parseFloat(bodyFatStr);
            if (bodyFat<=0){
                bodyFatET.setError(getText(R.string.noPositive));
                bodyFatET.requestFocus();
                return false;
            }
        }

        EditText waterET = getActivity().findViewById(R.id.water);
        String waterStr = waterET.getText().toString();
        if (waterStr.trim().isEmpty()) {
            water=-23;
        } else {
            water = Float.parseFloat(waterStr);
            if (water<=0){
                waterET.setError(getText(R.string.noPositive));
                waterET.requestFocus();
                return false;
            }
        }

        comment = ((EditText)getActivity().findViewById(R.id.comment)).getText().toString();
        if (comment.equals("")){
            comment = null;
        }
        return true;
    }
}