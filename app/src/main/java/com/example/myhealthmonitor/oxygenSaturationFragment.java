package com.example.myhealthmonitor;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class oxygenSaturationFragment extends ReportFragment {

    private View view;
    private DBHelper dbh;
    private String date;
    private String time;
    private int avg;
    private int max;
    private int min;
    private int attention;
    private String comment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_oxygen_saturation, container, false);
        return view;
    }

    @Override
    public void onStart() {
        setPickers(getActivity().findViewById(R.id.date), getActivity().findViewById(R.id.time));
        dbh = MainActivity.getDBHelper();

        upload();
        super.onStart();
    }

    @Override
    void delete() {
        String idTuple = Integer.toString(getActivity().getIntent().getExtras().getInt("id"));
        boolean code = dbh.deleteOxygenReport(idTuple);
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
            boolean code = dbh.modifyOxygenReport(idTuple, date, time, attention, avg, max, min, comment);
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
            boolean code = dbh.insertNewOxygenReport(date, time, attention, avg, max, min, comment);
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
            Cursor cursor = dbh.getOxygenReport(Integer.toString(idTuple));
            cursor.moveToFirst();

            if (!cursor.isAfterLast()) {

                setDateInCorrectForm(view.findViewById(R.id.date), cursor.getString(cursor.getColumnIndex(DBHelper.O_DATE)));
                setTimeInCorrectForm(view.findViewById(R.id.time), cursor.getString(cursor.getColumnIndex(DBHelper.O_TIME)));

                ((Spinner)getActivity().findViewById(R.id.attention)).setSelection(cursor.getInt(cursor.getColumnIndex(DBHelper.G_ATTENTION)));

                EditText avgET = view.findViewById(R.id.SpO);
                String avg = cursor.getString(cursor.getColumnIndex(DBHelper.O_AVG));
                avgET.setText(avg);

                EditText maxET = view.findViewById(R.id.SpOMax);
                String max = cursor.getString(cursor.getColumnIndex(DBHelper.O_MAX));
                maxET.setText(max);

                EditText minET = view.findViewById(R.id.SpOMin);
                String min = cursor.getString(cursor.getColumnIndex(DBHelper.O_MIN));
                minET.setText(min);

                EditText commET = view.findViewById(R.id.comment);
                String comm = cursor.getString(cursor.getColumnIndex(DBHelper.O_COMMENT));
                commET.setText(comm);
            }
            cursor.close();
        }
    }

    private boolean getData(){
        //get Report date
        date = getDate();
        System.out.println("Date in gg/mm/aaaa => " + date);

        time = getTime();
        System.out.println("Time in kk:mm => " + time);

        attention = ((Spinner)getActivity().findViewById(R.id.attention)).getSelectedItemPosition();

        EditText avgET = getActivity().findViewById(R.id.SpO);
        String avgStr = (avgET).getText().toString();
        if (avgStr.trim().isEmpty()) {
            avgET.setError(getText(R.string.required));
            avgET.requestFocus();
            return false;
        } else {
            avg = Integer.parseInt(avgStr);
            if (avg<=0){
                avgET.setError(getText(R.string.noPositive));
                avgET.requestFocus();
                return false;
            }
        }

        EditText maxET = getActivity().findViewById(R.id.SpOMax);
        String maxStr = maxET.getText().toString();
        if (maxStr.trim().isEmpty()) {
            max=-23;
        } else {
            max = Integer.parseInt(maxStr);
            if (max<=0){
                maxET.setError(getText(R.string.noPositive));
                maxET.requestFocus();
                return false;
            }
        }

        EditText minET = getActivity().findViewById(R.id.SpOMin);
        String minStr = minET.getText().toString();
        if (minStr.trim().isEmpty()) {
            min=-23;
        } else {
            min = Integer.parseInt(minStr);
            if (min<=0){
                minET.setError(getText(R.string.noPositive));
                minET.requestFocus();
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