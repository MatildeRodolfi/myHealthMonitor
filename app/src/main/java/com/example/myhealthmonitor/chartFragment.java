package com.example.myhealthmonitor;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

public class chartFragment extends Fragment {

    private SharedPreferences pref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setPicker();

        pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
        Spinner spinner = getActivity().findViewById(R.id.type_report);
        ArrayList<String> listType = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.typeReportAbbreviationPlusGeneral)));
        String type = pref.getString(MainActivity.TYPECHARTS, null);
        int pos = listType.indexOf(type);
        spinner.setSelection(pos);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                String val = getResources().getStringArray(R.array.typeReportAbbreviationPlusGeneral)[position];
                System.out.println("Val: "+val);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(MainActivity.TYPECHARTS,val);
                editor.apply();
                loadDate();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        getActivity().findViewById(R.id.filter).setOnClickListener(v -> spinner.performClick());
        super.onViewCreated(view, savedInstanceState);
    }

    //set Pickers (call by fragment)
    protected void setPicker(){
        Calendar myCalendar = Calendar.getInstance();

        //onClick on dateET open DatePicker in a Dialog
        ImageView dateIV = getActivity().findViewById(R.id.iconDate);

        dateIV.setOnClickListener(v -> {
            long begin = 0L;
            long end = 0L;
            MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
            pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
            try {
                myCalendar.setTime(format.parse(pref.getString(MainActivity.DATECHARTSFIRST, null)));
                begin = myCalendar.getTimeInMillis();
                myCalendar.setTime(format.parse(pref.getString(MainActivity.DATECHARTSLAST, null)));
                end = myCalendar.getTimeInMillis();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            builder.setSelection(Pair.create(begin, end));
            final MaterialDatePicker<Pair<Long, Long>> picker = builder.build();
            picker.show(getFragmentManager(), picker.toString());

            picker.addOnPositiveButtonClickListener(x->{
                Pair<Long, Long> res = picker.getSelection();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(MainActivity.DATECHARTSFIRST, dateFormat.format(res.first));
                editor.putString(MainActivity.DATECHARTSLAST, dateFormat.format(res.second));
                System.out.println("Begin: "+dateFormat.format(res.first)+ " end"+dateFormat.format(res.second));
                editor.apply();
                loadDate();
            });
        });
    }

    private void loadDate(){
        DBHelper dbh = MainActivity.getDBHelper();

        pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
        String firstDay = pref.getString(MainActivity.DATECHARTSFIRST, null);
        String lastDay = pref.getString(MainActivity.DATECHARTSLAST, null);
        TextView title = getActivity().findViewById(R.id.title);
        ImageView icon = getActivity().findViewById(R.id.icon);
        Cursor cursor;
        TextView noReport = getView().findViewById(R.id.no_report_chart);
        switch (pref.getString(MainActivity.TYPECHARTS, null)){
            case "GEN":
                title.setText(R.string.general);
                icon.setImageResource(R.drawable.ic_description);
                Cursor cursorPA = dbh.getPhysicalActivityReportsOverPeriod(firstDay, lastDay);
                Cursor cursorG = dbh.getGlycemiaReportsOverPeriod(firstDay, lastDay);
                Cursor cursorW = dbh.getWeightReportsOverPeriod(firstDay, lastDay);
                Cursor cursorP = dbh.getPulseReportsOverPeriod(firstDay, lastDay);
                Cursor cursorO = dbh.getOxygenReportsOverPeriod(firstDay, lastDay);
                Cursor cursorT = dbh.getTemperatureReportsOverPeriod(firstDay, lastDay);
                cursorPA.moveToFirst();
                cursorG.moveToFirst();
                cursorW.moveToFirst();
                cursorP.moveToFirst();
                cursorO.moveToFirst();
                cursorT.moveToFirst();
                if (!cursorPA.isAfterLast() || !cursorG.isAfterLast() || !cursorW.isAfterLast() || !cursorP.isAfterLast() || !cursorO.isAfterLast() || !cursorT.isAfterLast()){
                    noReport.setVisibility(View.GONE);

                    TextView reportTimeTitle = getActivity().findViewById(R.id.titleChart1);
                    reportTimeTitle.setVisibility(View.VISIBLE);
                    reportTimeTitle.setText(R.string.reportTime);
                    getView().findViewById(R.id.chart1).setVisibility(View.VISIBLE);
                    if (firstDay.equals(lastDay)){
                        ChartLine reportTime = new ChartLine(getView().findViewById(R.id.chart1), getContext());
                        reportTime.setData(cursorPA, cursorG, cursorW, cursorP, cursorO, cursorT, firstDay, lastDay);
                    }
                    else {
                        ChartBar reportTime = new ChartBar(getView().findViewById(R.id.chart1), getContext());
                        reportTime.setData(cursorPA, cursorG, cursorW, cursorP, cursorO, cursorT, "ALL", firstDay, lastDay);
                    }

                    TextView typeReportTitle = getActivity().findViewById(R.id.titleChart2);
                    typeReportTitle.setVisibility(View.VISIBLE);
                    typeReportTitle.setText(R.string.typeReport);
                    getView().findViewById(R.id.chart2).setVisibility(View.VISIBLE);
                    ChartPie typeReport = new ChartPie(getView().findViewById(R.id.chart2), getContext());
                    typeReport.setData(cursorPA, cursorG, cursorW, cursorP, cursorO, cursorT);

                    TextView typeReportTimeTitle = getActivity().findViewById(R.id.titleChart3);
                    typeReportTimeTitle.setVisibility(View.VISIBLE);
                    typeReportTimeTitle.setText(R.string.typeReportTime);
                    getView().findViewById(R.id.chart3).setVisibility(View.VISIBLE);
                    ChartBar typeReportTime = new ChartBar(getView().findViewById(R.id.chart3), getContext());
                    typeReportTime.setData(cursorPA, cursorG, cursorW, cursorP, cursorO, cursorT, "TYPE", firstDay, lastDay);
                }
                else{
                    noReport.setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.titleChart1).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.chart1).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.titleChart2).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.chart2).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.titleChart3).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.chart3).setVisibility(View.GONE);
                }

                getActivity().findViewById(R.id.titleChart4).setVisibility(View.GONE);
                getActivity().findViewById(R.id.chart4).setVisibility(View.GONE);
                getActivity().findViewById(R.id.titleChart5).setVisibility(View.GONE);
                getActivity().findViewById(R.id.chart5).setVisibility(View.GONE);
                getActivity().findViewById(R.id.titleChart6).setVisibility(View.GONE);
                getActivity().findViewById(R.id.chart6).setVisibility(View.GONE);
                break;
            case "PA":
                title.setText(R.string.physicalActivity);
                icon.setImageResource(R.drawable.img_shoe);

                cursor = dbh.getPhysicalActivityReportsOverPeriod(firstDay, lastDay);
                cursor.moveToFirst();
                if (!cursor.isAfterLast()){
                    noReport.setVisibility(View.GONE);

                    TextView kcalTitle = getActivity().findViewById(R.id.titleChart1);
                    kcalTitle.setVisibility(View.VISIBLE);
                    kcalTitle.setText(R.string.calories);
                    getView().findViewById(R.id.chart1).setVisibility(View.VISIBLE);
                    if (firstDay.equals(lastDay)){
                        ChartLine kcal = new ChartLine(getView().findViewById(R.id.chart1), getContext());
                        kcal.setData(cursor, DBHelper.PA_CALORIES, firstDay, lastDay);
                    }
                    else{
                        ChartBar kcal = new ChartBar(getView().findViewById(R.id.chart1), getContext());
                        kcal.setData(cursor, DBHelper.PA_CALORIES, firstDay, lastDay);
                    }

                    TextView stepsTitle = getActivity().findViewById(R.id.titleChart2);
                    stepsTitle.setVisibility(View.VISIBLE);
                    stepsTitle.setText(R.string.steps);
                    getView().findViewById(R.id.chart2).setVisibility(View.VISIBLE);
                    ChartLine steps = new ChartLine(getView().findViewById(R.id.chart2), getContext());
                    steps.setData(cursor, DBHelper.PA_STEPS, firstDay, lastDay);

                    TextView distanceTitle = getActivity().findViewById(R.id.titleChart3);
                    distanceTitle.setVisibility(View.VISIBLE);
                    distanceTitle.setText(R.string.distance);
                    getView().findViewById(R.id.chart3).setVisibility(View.VISIBLE);
                    ChartLine distance = new ChartLine(getView().findViewById(R.id.chart3), getContext());
                    distance.setData(cursor, DBHelper.PA_DISTANCE, firstDay, lastDay);

                    TextView durationTitle = getActivity().findViewById(R.id.titleChart4);
                    durationTitle.setVisibility(View.VISIBLE);
                    durationTitle.setText(R.string.duration);
                    getView().findViewById(R.id.chart4).setVisibility(View.VISIBLE);
                    ChartLine duration = new ChartLine(getView().findViewById(R.id.chart4), getContext());
                    duration.setData(cursor, DBHelper.PA_DURATION, firstDay, lastDay);
                }
                else{
                    noReport.setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.titleChart1).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.chart1).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.titleChart2).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.chart2).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.titleChart3).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.chart3).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.titleChart4).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.chart4).setVisibility(View.GONE);
                }

                getActivity().findViewById(R.id.titleChart5).setVisibility(View.GONE);
                getActivity().findViewById(R.id.chart5).setVisibility(View.GONE);
                getActivity().findViewById(R.id.titleChart6).setVisibility(View.GONE);
                getActivity().findViewById(R.id.chart6).setVisibility(View.GONE);
                break;
            case "G":
                title.setText(R.string.glycemia);
                icon.setImageResource(R.drawable.img_drop);

                cursor = dbh.getGlycemiaReportsOverPeriod(firstDay, lastDay);
                cursor.moveToFirst();
                if (!cursor.isAfterLast()){
                    noReport.setVisibility(View.GONE);

                    TextView valueGTitle = getActivity().findViewById(R.id.titleChart1);
                    valueGTitle.setVisibility(View.VISIBLE);
                    valueGTitle.setText(R.string.glycemiaVal);
                    getView().findViewById(R.id.chart1).setVisibility(View.VISIBLE);
                    ChartLine valueG = new ChartLine(getView().findViewById(R.id.chart1), getContext());
                    valueG.setData(cursor, DBHelper.G_VALUE, firstDay, lastDay);

                    TextView labelTitle = getActivity().findViewById(R.id.titleChart2);
                    labelTitle.setVisibility(View.VISIBLE);
                    labelTitle.setText(R.string.label);
                    getView().findViewById(R.id.chart2).setVisibility(View.VISIBLE);
                    ChartPie label = new ChartPie(getView().findViewById(R.id.chart2), getContext());
                    label.setData(cursor, DBHelper.G_LABEL);

                    TextView HbA1CTitle = getActivity().findViewById(R.id.titleChart3);
                    HbA1CTitle.setVisibility(View.VISIBLE);
                    HbA1CTitle.setText(R.string.HbA1C);
                    getView().findViewById(R.id.chart3).setVisibility(View.VISIBLE);
                    ChartLine HbA1C = new ChartLine(getView().findViewById(R.id.chart3), getContext());
                    HbA1C.setData(cursor, DBHelper.G_HbA1C, firstDay, lastDay);

                    TextView breadUnitTitle = getActivity().findViewById(R.id.titleChart4);
                    breadUnitTitle.setVisibility(View.VISIBLE);
                    breadUnitTitle.setText(R.string.breadUnit);
                    getView().findViewById(R.id.chart4).setVisibility(View.VISIBLE);
                    ChartLine breadUnit = new ChartLine(getView().findViewById(R.id.chart4), getContext());
                    breadUnit.setData(cursor, DBHelper.G_BREAD_UNIT, firstDay, lastDay);

                    TextView bolusTitle = getActivity().findViewById(R.id.titleChart5);
                    bolusTitle.setVisibility(View.VISIBLE);
                    bolusTitle.setText(R.string.bolus);
                    getView().findViewById(R.id.chart5).setVisibility(View.VISIBLE);
                    ChartLine bolus = new ChartLine(getView().findViewById(R.id.chart5), getContext());
                    bolus.setData(cursor, DBHelper.G_BOLUS, firstDay, lastDay);

                    TextView basalTitle = getActivity().findViewById(R.id.titleChart6);
                    basalTitle.setVisibility(View.VISIBLE);
                    basalTitle.setText(R.string.basal);
                    getView().findViewById(R.id.chart6).setVisibility(View.VISIBLE);
                    ChartLine basal = new ChartLine(getView().findViewById(R.id.chart6), getContext());
                    basal.setData(cursor, DBHelper.G_BASAL, firstDay, lastDay);
                }
                else{
                    noReport.setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.titleChart1).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.chart1).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.titleChart2).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.chart2).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.titleChart3).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.chart3).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.titleChart4).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.chart4).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.titleChart5).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.chart5).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.titleChart6).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.chart6).setVisibility(View.GONE);
                }
                break;
            case "W":
                title.setText(R.string.weight);
                icon.setImageResource(R.drawable.img_weight);

                cursor = dbh.getWeightReportsOverPeriod(firstDay, lastDay);
                cursor.moveToFirst();
                if (!cursor.isAfterLast()){
                    noReport.setVisibility(View.GONE);

                    TextView valueWTitle = getActivity().findViewById(R.id.titleChart1);
                    valueWTitle.setVisibility(View.VISIBLE);
                    valueWTitle.setText(R.string.weight);
                    getView().findViewById(R.id.chart1).setVisibility(View.VISIBLE);
                    ChartLine valueW = new ChartLine(getView().findViewById(R.id.chart1), getContext());
                    valueW.setData(cursor, DBHelper.W_VALUE, firstDay, lastDay);

                    TextView musclesTitle = getActivity().findViewById(R.id.titleChart2);
                    musclesTitle.setVisibility(View.VISIBLE);
                    musclesTitle.setText(R.string.muscles);
                    getView().findViewById(R.id.chart2).setVisibility(View.VISIBLE);
                    ChartLine muscles = new ChartLine(getView().findViewById(R.id.chart2), getContext());
                    muscles.setData(cursor, DBHelper.W_MUSCLES, firstDay, lastDay);

                    TextView bodyFatTitle = getActivity().findViewById(R.id.titleChart3);
                    bodyFatTitle.setVisibility(View.VISIBLE);
                    bodyFatTitle.setText(R.string.bodyFat);
                    getView().findViewById(R.id.chart3).setVisibility(View.VISIBLE);
                    ChartLine bodyFat = new ChartLine(getView().findViewById(R.id.chart3), getContext());
                    bodyFat.setData(cursor, DBHelper.W_BODY_FAT, firstDay, lastDay);

                    TextView waterTitle = getActivity().findViewById(R.id.titleChart4);
                    waterTitle.setVisibility(View.VISIBLE);
                    waterTitle.setText(R.string.water);
                    getView().findViewById(R.id.chart4).setVisibility(View.VISIBLE);
                    ChartLine water = new ChartLine(getView().findViewById(R.id.chart4), getContext());
                    water.setData(cursor, DBHelper.W_WATER, firstDay, lastDay);
                }
                else{
                    noReport.setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.titleChart1).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.chart1).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.titleChart2).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.chart2).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.titleChart3).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.chart3).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.titleChart4).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.chart4).setVisibility(View.GONE);
                }

                getActivity().findViewById(R.id.titleChart5).setVisibility(View.GONE);
                getActivity().findViewById(R.id.chart5).setVisibility(View.GONE);
                getActivity().findViewById(R.id.titleChart6).setVisibility(View.GONE);
                getActivity().findViewById(R.id.chart6).setVisibility(View.GONE);
                break;
            case "P":
                title.setText(R.string.pulse);
                icon.setImageResource(R.drawable.img_heart_beat);

                cursor = dbh.getPulseReportsOverPeriod(firstDay, lastDay);
                cursor.moveToFirst();
                if (!cursor.isAfterLast()){
                    noReport.setVisibility(View.GONE);

                    TextView avgPulseTitle = getActivity().findViewById(R.id.titleChart1);
                    avgPulseTitle.setVisibility(View.VISIBLE);
                    avgPulseTitle.setText(R.string.avgPulse);
                    getView().findViewById(R.id.chart1).setVisibility(View.VISIBLE);
                    ChartLineMinMaxAvg pulse = new ChartLineMinMaxAvg(getView().findViewById(R.id.chart1), getContext());
                    pulse.setData(cursor, DBHelper.P_MIN, DBHelper.P_MAX, DBHelper.P_AVG, firstDay, lastDay);
                }
                else{
                    noReport.setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.titleChart1).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.chart1).setVisibility(View.GONE);
                }

                getActivity().findViewById(R.id.titleChart2).setVisibility(View.GONE);
                getActivity().findViewById(R.id.chart2).setVisibility(View.GONE);
                getActivity().findViewById(R.id.titleChart3).setVisibility(View.GONE);
                getActivity().findViewById(R.id.chart3).setVisibility(View.GONE);
                getActivity().findViewById(R.id.titleChart4).setVisibility(View.GONE);
                getActivity().findViewById(R.id.chart4).setVisibility(View.GONE);
                getActivity().findViewById(R.id.titleChart5).setVisibility(View.GONE);
                getActivity().findViewById(R.id.chart5).setVisibility(View.GONE);
                getActivity().findViewById(R.id.titleChart6).setVisibility(View.GONE);
                getActivity().findViewById(R.id.chart6).setVisibility(View.GONE);
                break;
            case "O":
                title.setText(R.string.oxygenSaturation);
                icon.setImageResource(R.drawable.img_oxygen);

                cursor = dbh.getOxygenReportsOverPeriod(firstDay, lastDay);
                cursor.moveToFirst();
                if (!cursor.isAfterLast()){
                    noReport.setVisibility(View.GONE);

                    TextView SpOTitle = getActivity().findViewById(R.id.titleChart1);
                    SpOTitle.setVisibility(View.VISIBLE);
                    SpOTitle.setText(R.string.SpO);
                    getView().findViewById(R.id.chart1).setVisibility(View.VISIBLE);
                    ChartLineMinMaxAvg SpO = new ChartLineMinMaxAvg(getView().findViewById(R.id.chart1), getContext());
                    SpO.setData(cursor, DBHelper.O_MIN, DBHelper.O_MAX, DBHelper.O_AVG, firstDay, lastDay);
                }
                else{
                    noReport.setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.titleChart1).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.chart1).setVisibility(View.GONE);
                }

                getActivity().findViewById(R.id.titleChart2).setVisibility(View.GONE);
                getActivity().findViewById(R.id.chart2).setVisibility(View.GONE);
                getActivity().findViewById(R.id.titleChart3).setVisibility(View.GONE);
                getActivity().findViewById(R.id.chart3).setVisibility(View.GONE);
                getActivity().findViewById(R.id.titleChart4).setVisibility(View.GONE);
                getActivity().findViewById(R.id.chart4).setVisibility(View.GONE);
                getActivity().findViewById(R.id.titleChart5).setVisibility(View.GONE);
                getActivity().findViewById(R.id.chart5).setVisibility(View.GONE);
                getActivity().findViewById(R.id.titleChart6).setVisibility(View.GONE);
                getActivity().findViewById(R.id.chart6).setVisibility(View.GONE);
                break;
            case "T":
                title.setText(R.string.temperature);
                icon.setImageResource(R.drawable.img_thermometer);

                cursor = dbh.getTemperatureReportsOverPeriod(firstDay, lastDay);
                cursor.moveToFirst();
                if (!cursor.isAfterLast()){
                    noReport.setVisibility(View.GONE);

                    TextView valTTitle = getActivity().findViewById(R.id.titleChart1);
                    valTTitle.setVisibility(View.VISIBLE);
                    valTTitle.setText(R.string.temperature);
                    getView().findViewById(R.id.chart1).setVisibility(View.VISIBLE);
                    ChartLine valT = new ChartLine(getView().findViewById(R.id.chart1), getContext());
                    valT.setData(cursor, DBHelper.T_VALUE, firstDay, lastDay);
                }
                else{
                    noReport.setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.titleChart1).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.chart1).setVisibility(View.GONE);
                }

                getActivity().findViewById(R.id.titleChart2).setVisibility(View.GONE);
                getActivity().findViewById(R.id.chart2).setVisibility(View.GONE);
                getActivity().findViewById(R.id.titleChart3).setVisibility(View.GONE);
                getActivity().findViewById(R.id.chart3).setVisibility(View.GONE);
                getActivity().findViewById(R.id.titleChart4).setVisibility(View.GONE);
                getActivity().findViewById(R.id.chart4).setVisibility(View.GONE);
                getActivity().findViewById(R.id.titleChart5).setVisibility(View.GONE);
                getActivity().findViewById(R.id.chart5).setVisibility(View.GONE);
                getActivity().findViewById(R.id.titleChart6).setVisibility(View.GONE);
                getActivity().findViewById(R.id.chart6).setVisibility(View.GONE);
                break;
        }
    }


}
