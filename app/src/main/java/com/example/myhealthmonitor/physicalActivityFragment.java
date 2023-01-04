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

public class physicalActivityFragment extends ReportFragment {

    private View view;
    private DBHelper dbh;
    private String date;
    private String time;
    private int attention;
    private int steps;
    private int calories;
    private float distance;
    private int duration;
    private String comment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_physical_activity, container, false);
        return view;
    }

    @Override
    public void onStart() {
        setPickers(getActivity().findViewById(R.id.date), getActivity().findViewById(R.id.time));

        pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.getBoolean(MainActivity.LENGTHEDEF,true)) {
            ((TextView) getActivity().findViewById(R.id.distanceMU)).setText(getResources().getString(R.string.length1));
        } else{
            ((TextView) getActivity().findViewById(R.id.distanceMU)).setText(getResources().getString(R.string.length2));
        }

        dbh = MainActivity.getDBHelper();

        upload();
        super.onStart();
    }

    @Override
    void delete() {
        String idTuple = Integer.toString(getActivity().getIntent().getExtras().getInt("id"));
        boolean code = dbh.deletePhysicalActivityReport(idTuple);
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
            boolean code = dbh.modifyPhysicalActivityReport(idTuple, date, time, attention, steps, calories, distance, duration, comment);
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
            boolean code = dbh.insertNewPhysicalActivityReport(date, time, attention, steps, calories, distance, duration, comment);
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
            Cursor cursor = dbh.getPhysicalActivityReport(Integer.toString(idTuple));
            cursor.moveToFirst();

            if (!cursor.isAfterLast()) {

                setDateInCorrectForm(view.findViewById(R.id.date), cursor.getString(cursor.getColumnIndex(DBHelper.PA_DATE)));
                setTimeInCorrectForm(view.findViewById(R.id.time), cursor.getString(cursor.getColumnIndex(DBHelper.PA_TIME)));

                ((Spinner)getActivity().findViewById(R.id.attention)).setSelection(cursor.getInt(cursor.getColumnIndex(DBHelper.G_ATTENTION)));

                EditText caloriesET = view.findViewById(R.id.calories);
                String calories = cursor.getString(cursor.getColumnIndex(DBHelper.PA_CALORIES));
                caloriesET.setText(calories);

                EditText stepsET = view.findViewById(R.id.steps);
                String steps = cursor.getString(cursor.getColumnIndex(DBHelper.PA_STEPS));
                stepsET.setText(steps);

                EditText distanceET = view.findViewById(R.id.distance);
                String distance = getLengthInCorrectForm(cursor.getString(cursor.getColumnIndex(DBHelper.PA_DISTANCE)));
                distanceET.setText(distance);

                EditText durationET = view.findViewById(R.id.duration);
                String duration = cursor.getString(cursor.getColumnIndex(DBHelper.PA_DURATION));
                durationET.setText(duration);

                EditText commET = view.findViewById(R.id.comment);
                String comm = cursor.getString(cursor.getColumnIndex(DBHelper.PA_COMMENT));
                commET.setText(comm);
            }
            cursor.close();
        }
    }

    //visualize length on Pref unit of measure
    private String getLengthInCorrectForm(String distance) {
        if (distance==null){
            return null;
        }
        pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
        if (!pref.getBoolean(MainActivity.LENGTHEDEF, true)) {
            float dist;
            dist = Float.parseFloat(distance);
            dist = (float) (dist / 1.609344);
            return format(Locale.US, MainActivity.FORMAT, dist);
        } else {
            return format(Locale.US, MainActivity.FORMAT, Float.parseFloat(distance));
        }
    }

    private boolean getData(){
        //get Report date
        date = getDate();
        System.out.println("Date in gg/mm/aaaa => " + date);

        time = getTime();
        System.out.println("Time in kk:mm => " + time);

        attention = ((Spinner)getActivity().findViewById(R.id.attention)).getSelectedItemPosition();

        EditText caloriesET = getActivity().findViewById(R.id.calories);
        String caloriesStr = (caloriesET).getText().toString();
        if (caloriesStr.trim().isEmpty()) {
            caloriesET.setError(getText(R.string.required));
            caloriesET.requestFocus();
            return false;
        } else {
            calories = Integer.parseInt(caloriesStr);
            if (calories<=0){
                caloriesET.setError(getText(R.string.noPositive));
                caloriesET.requestFocus();
                return false;
            }
        }

        EditText stepsET = getActivity().findViewById(R.id.steps);
        String stepsStr = stepsET.getText().toString();
        if (stepsStr.trim().isEmpty()) {
            steps=-23;
        } else {
            steps = Integer.parseInt(stepsStr);
            if (steps<=0){
                stepsET.setError(getText(R.string.noPositive));
                stepsET.requestFocus();
                return false;
            }
        }

        EditText distanceET = getActivity().findViewById(R.id.distance);
        String distanceStr = distanceET.getText().toString();
        if (distanceStr.trim().isEmpty()) {
            distance=-23;
        } else {
            distance = Float.parseFloat(distanceStr);
            if (distance<=0){
                distanceET.setError(getText(R.string.noPositive));
                distanceET.requestFocus();
                return false;
            }
            pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
            if (!pref.getBoolean(MainActivity.LENGTHEDEF,true)) {
                distance = (float) (distance * 1.609344);
            }
            System.out.println("Distance value in km => " + distance);
        }

        EditText durationET = getActivity().findViewById(R.id.duration);
        String durationStr = durationET.getText().toString();
        if (durationStr.trim().isEmpty()) {
            duration=-23;
        } else {
            duration = Integer.parseInt(durationStr);
            if (duration<=0){
                durationET.setError(getText(R.string.noPositive));
                durationET.requestFocus();
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