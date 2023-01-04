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

public class glycemiaFragment extends ReportFragment {

    private View view;
    private DBHelper dbh;
    private String date;
    private String time;
    private float value;
    private int label;
    private float HbA1C;
    private int breadUnit;
    private float bolus;
    private float basal;
    private int attention;
    private String comment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_glycemia, container, false);
        return view;
    }

    @Override
    public void onStart() {
        setPickers(getActivity().findViewById(R.id.date), getActivity().findViewById(R.id.time));

        pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.getBoolean(MainActivity.GLYCEMIADEF,true)) {
            ((TextView) getActivity().findViewById(R.id.glycemiaMU)).setText(getResources().getString(R.string.glycemia1));
        } else{
            ((TextView) getActivity().findViewById(R.id.glycemiaMU)).setText(getResources().getString(R.string.glycemia2));
        }

        dbh = MainActivity.getDBHelper();

        upload();
        super.onStart();
    }

    @Override
    void delete() {
        String idTuple = Integer.toString(getActivity().getIntent().getExtras().getInt("id"));
        boolean code = dbh.deleteGlycemiaReport(idTuple);
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
        if (getData()){
            //save Report date in db
            String idTuple = Integer.toString(getActivity().getIntent().getExtras().getInt("id"));
            boolean code = dbh.modifyGlycemiaReport(idTuple, date, time, attention, value, label, HbA1C, breadUnit, bolus, basal, comment);
            if (code) {
                Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.saved), Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
            else{
                new AlertDialog.Builder(getContext())
                        .setMessage(getResources().getString(R.string.error))
                        .setPositiveButton(android.R.string.yes, null)
                        .show();
            }
        }
    }

    @Override
    void save() {
        if (getData()){
            //save Report date in db
            boolean code = dbh.insertNewGlycemiaReport(date, time, attention, value, label, HbA1C, breadUnit, bolus, basal, comment);
            if (code) {
                Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.saved), Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
            else{
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
            Cursor cursor = dbh.getGlycemiaReport(Integer.toString(idTuple));
            cursor.moveToFirst();

            if (!cursor.isAfterLast()) {

                setDateInCorrectForm(view.findViewById(R.id.date), cursor.getString(cursor.getColumnIndex(DBHelper.G_DATE)));
                setTimeInCorrectForm(view.findViewById(R.id.time), cursor.getString(cursor.getColumnIndex(DBHelper.G_TIME)));

                ((Spinner)getActivity().findViewById(R.id.attention)).setSelection(cursor.getInt(cursor.getColumnIndex(DBHelper.G_ATTENTION)));

                EditText valueET = view.findViewById(R.id.glycemiaVal);
                String value = getGlycemiaInCorrectForm(cursor.getString(cursor.getColumnIndex(DBHelper.G_VALUE)));
                valueET.setText(value);

                ((Spinner)getActivity().findViewById(R.id.label)).setSelection(cursor.getInt(cursor.getColumnIndex(DBHelper.G_LABEL)));

                EditText HbA1CET = view.findViewById(R.id.HbA1C);
                String HbA1C = cursor.getString(cursor.getColumnIndex(DBHelper.G_HbA1C));
                HbA1CET.setText(HbA1C);

                EditText breadUnitET = view.findViewById(R.id.breadUnit);
                String breadUnit = cursor.getString(cursor.getColumnIndex(DBHelper.G_BREAD_UNIT));
                breadUnitET.setText(breadUnit);

                EditText bolusET = view.findViewById(R.id.bolus);
                String bolus = cursor.getString(cursor.getColumnIndex(DBHelper.G_BOLUS));
                bolusET.setText(bolus);

                EditText basalET = view.findViewById(R.id.basal);
                String basal = cursor.getString(cursor.getColumnIndex(DBHelper.G_BASAL));
                basalET.setText(basal);

                EditText commET = view.findViewById(R.id.comment);
                String comm = cursor.getString(cursor.getColumnIndex(DBHelper.G_COMMENT));
                commET.setText(comm);
            }
            cursor.close();
        }
    }

    //visualize glycemia on Pref unit of measure
    private String getGlycemiaInCorrectForm(String glycemia) {
        pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
        if (!pref.getBoolean(MainActivity.GLYCEMIADEF, true)) {
            float g;
            g = Float.parseFloat(glycemia);
            g = (float) (g / 18.0182);
            return format(Locale.US, MainActivity.FORMAT, g);
        } else {
            return format(Locale.US, MainActivity.FORMAT, Float.parseFloat(glycemia));
        }
    }

    private boolean getData(){
        //get Report date
        date = getDate();
        System.out.println("Date in gg/mm/aaaa => " + date);

        time = getTime();
        System.out.println("Time in kk:mm => " + time);

        attention = ((Spinner)getActivity().findViewById(R.id.attention)).getSelectedItemPosition();

        EditText valueET = getActivity().findViewById(R.id.glycemiaVal);
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
            if (!pref.getBoolean(MainActivity.GLYCEMIADEF,true)) {
                value = (float) (value * 18.0182);
            }
            System.out.println("Glycemia value in mg/dL => " + value);
        }

        label = ((Spinner)getActivity().findViewById(R.id.label)).getSelectedItemPosition();

        EditText HbA1CET = getActivity().findViewById(R.id.HbA1C);
        String HbA1CStr = HbA1CET.getText().toString();
        if (HbA1CStr.trim().isEmpty()) {
            HbA1C=-23;
        } else {
            HbA1C = Float.parseFloat(HbA1CStr);
            if (HbA1C<=0){
                HbA1CET.setError(getText(R.string.noPositive));
                HbA1CET.requestFocus();
                return false;
            }
        }

        EditText breadUnitET = getActivity().findViewById(R.id.breadUnit);
        String breadUnitStr = breadUnitET.getText().toString();
        if (breadUnitStr.trim().isEmpty()) {
            breadUnit=-23;
        } else {
            breadUnit = Integer.parseInt(breadUnitStr);
            if (breadUnit<=0){
                breadUnitET.setError(getText(R.string.noPositive));
                breadUnitET.requestFocus();
                return false;
            }
        }

        EditText bolusET = getActivity().findViewById(R.id.bolus);
        String bolusStr = bolusET.getText().toString();
        if (bolusStr.trim().isEmpty()) {
            bolus=-23;
        } else {
            bolus = Float.parseFloat(bolusStr);
            if (bolus<=0){
                bolusET.setError(getText(R.string.noPositive));
                bolusET.requestFocus();
                return false;
            }
        }

        EditText basalET = getActivity().findViewById(R.id.basal);
        String basalStr = basalET.getText().toString();
        if (basalStr.trim().isEmpty()) {
            basal=-23;
        } else {
            basal = Float.parseFloat(basalStr);
            if (basal<=0){
                basalET.setError(getText(R.string.noPositive));
                basalET.requestFocus();
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