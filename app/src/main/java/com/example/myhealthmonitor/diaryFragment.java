package com.example.myhealthmonitor;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import static java.lang.String.format;

public class diaryFragment extends Fragment {
    private SharedPreferences pref;

    //FAB menu items
    private FloatingActionButton FabPhysicalActivity;
    private FloatingActionButton FabGlycemia;
    private FloatingActionButton FabWeight;
    private FloatingActionButton FabPulse;
    private FloatingActionButton FabOxygenSaturation;
    private FloatingActionButton FabTemperature;
    private boolean isFABOpen=false;

    private final HashMap<String, Integer> idMap= new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        setFABMenu();

        setOpenerCloser();

        setPicker();

        TextView dataET = getView().findViewById(R.id.date);
        dataET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                loadReports();
            }
        });

        getActivity().findViewById(R.id.filter).setOnClickListener(v -> {
            DialogFilterFragment dialogFragment = new DialogFilterFragment();

            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            dialogFragment.show(ft, "dialog");
        });
    }

    @Override
    public void onPause() {
        //close FAB menu if open
        closeFABMenu();
        super.onPause();
    }

    @Override
    public void onResume() {
        loadReports();
        super.onResume();
    }

    //set on click Listener for items in FAB menu
    private void setFABMenu(){
        //get items for FAB menu
        FloatingActionButton fabReport = getView().findViewById(R.id.new_report);
        FabPhysicalActivity = getView().findViewById(R.id.new_physical_activity);
        FabGlycemia = getView().findViewById(R.id.new_glycemia);
        FabWeight =  getView().findViewById(R.id.new_weight);
        FabPulse =  getView().findViewById(R.id.new_pulse);
        FabOxygenSaturation =  getView().findViewById(R.id.new_oxygen_saturation);
        FabTemperature =  getView().findViewById(R.id.new_temperature);

        //set FAB Menu open/close on click on FabReport
        fabReport.setOnClickListener(thisView -> {
            if(!isFABOpen){
                showFABMenu();
            }else{
                closeFABMenu();
            }
        });

        //open NewReportActivity for new Physical Activity Report
        FabPhysicalActivity.setOnClickListener(thisView -> {
            Intent i = new Intent();
            i.setClass(getContext(), NewReportActivity.class);
            i.putExtra("type","PA");
            startActivity(i);
        });
        //open NewReportActivity for new Glycemia Report
        FabGlycemia.setOnClickListener(thisView -> {
            Intent i = new Intent();
            i.setClass(getContext(), NewReportActivity.class);
            i.putExtra("type","G");
            startActivity(i);
        });
        //open NewReportActivity for new Weight Report
        FabWeight.setOnClickListener(thisView -> {
            Intent i = new Intent();
            i.setClass(getContext(), NewReportActivity.class);
            i.putExtra("type","W");
            startActivity(i);
        });
        //open NewReportActivity for new FabPulse Report
        FabPulse.setOnClickListener(thisView -> {
            Intent i = new Intent();
            i.setClass(getContext(), NewReportActivity.class);
            i.putExtra("type","PU");
            startActivity(i);
        });
        //open NewReportActivity for new Oxygen Saturation Report
        FabOxygenSaturation.setOnClickListener(thisView -> {
            Intent i = new Intent();
            i.setClass(getContext(), NewReportActivity.class);
            i.putExtra("type","O");
            startActivity(i);
        });
        //open NewReportActivity for new Temperature Report
        FabTemperature.setOnClickListener(thisView -> {
            Intent i = new Intent();
            i.setClass(getContext(), NewReportActivity.class);
            i.putExtra("type","T");
            startActivity(i);
        });
    }

    //open FAB menu (items go up)
    private void showFABMenu(){
        float c = (float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT;
        isFABOpen=true;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //landscape
            FabPhysicalActivity.animate().translationY(-160*c);
            FabPhysicalActivity.animate().translationX(-60*c);
            FabGlycemia.animate().translationY(-110*c);
            FabGlycemia.animate().translationX(-60*c);
            FabWeight.animate().translationY(-60*c);
            FabWeight.animate().translationX(-60*c);
        } else {
            FabPhysicalActivity.animate().translationY(-310*c);
            FabGlycemia.animate().translationY(-260*c);
            FabWeight.animate().translationY(-210*c);
        }
        FabPulse.animate().translationY(-160*c);
        FabOxygenSaturation.animate().translationY(-110*c);
        FabTemperature.animate().translationY(-60*c);
    }

    //close FAB menu (items go down)
    private void closeFABMenu(){
        isFABOpen=false;
        FabPhysicalActivity.animate().translationY(0);
        FabGlycemia.animate().translationY(0);
        FabWeight.animate().translationY(0);
        FabPulse.animate().translationY(0);
        FabOxygenSaturation.animate().translationY(0);
        FabTemperature.animate().translationY(0);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            FabPhysicalActivity.animate().translationX(0);
            FabGlycemia.animate().translationX(0);
            FabWeight.animate().translationX(0);
        }
    }

    private void setOpenerCloser(){
        ImageView openerPA =  getView().findViewById(R.id.openPA);
        openerPA.setOnClickListener(thisView -> openReportsContainer(R.id.PAcont, R.id.openPA, R.id.closePA));

        ImageView closerPA =  getView().findViewById(R.id.closePA);
        closerPA.setOnClickListener(thisView -> closeReportsContainer(R.id.PAcont, R.id.openPA, R.id.closePA));

        ImageView openerG =  getView().findViewById(R.id.openG);
        openerG.setOnClickListener(thisView -> openReportsContainer(R.id.Gcont, R.id.openG, R.id.closeG));

        ImageView closerG =  getView().findViewById(R.id.closeG);
        closerG.setOnClickListener(thisView -> closeReportsContainer(R.id.Gcont, R.id.openG, R.id.closeG));

        ImageView openerW =  getView().findViewById(R.id.openW);
        openerW.setOnClickListener(thisView -> openReportsContainer(R.id.Wcont, R.id.openW, R.id.closeW));

        ImageView closerW =  getView().findViewById(R.id.closeW);
        closerW.setOnClickListener(thisView -> closeReportsContainer(R.id.Wcont, R.id.openW, R.id.closeW));

        ImageView openerP =  getView().findViewById(R.id.openP);
        openerP.setOnClickListener(thisView -> openReportsContainer(R.id.Pcont, R.id.openP, R.id.closeP));

        ImageView closerP =  getView().findViewById(R.id.closeP);
        closerP.setOnClickListener(thisView -> closeReportsContainer(R.id.Pcont, R.id.openP, R.id.closeP));

        ImageView openerO =  getView().findViewById(R.id.openO);
        openerO.setOnClickListener(thisView -> openReportsContainer(R.id.Ocont, R.id.openO, R.id.closeO));

        ImageView closerO =  getView().findViewById(R.id.closeO);
        closerO.setOnClickListener(thisView -> closeReportsContainer(R.id.Ocont, R.id.openO, R.id.closeO));

        ImageView openerT =  getView().findViewById(R.id.openT);
        openerT.setOnClickListener(thisView -> openReportsContainer(R.id.Tcont, R.id.openT, R.id.closeT));

        ImageView closerT =  getView().findViewById(R.id.closeT);
        closerT.setOnClickListener(thisView -> closeReportsContainer(R.id.Tcont, R.id.openT, R.id.closeT));
    }

    //open a container of Reports
    private void openReportsContainer(int idContainer, int idOpener, int idCloser) {
        getActivity().findViewById(idContainer).setVisibility(View.VISIBLE);
        ImageView opener =  getActivity().findViewById(idOpener);
        opener.setVisibility(View.INVISIBLE);
        ImageView closer =  getActivity().findViewById(idCloser);
        closer.setVisibility(View.VISIBLE);

    }

    //close a container of Reports
    private void closeReportsContainer(int idContainer, int idOpener, int idCloser) {
        getActivity().findViewById(idContainer).setVisibility(View.GONE);
        ImageView opener =  getActivity().findViewById(idOpener);
        opener.setVisibility(View.VISIBLE);
        ImageView closer =  getActivity().findViewById(idCloser);
        closer.setVisibility(View.INVISIBLE);
    }

    //set Pickers (call by fragment)
    protected void setPicker(){
        Calendar myCalendar = Calendar.getInstance();

        setDate();

        //onClick on dateET open DatePicker in a Dialog
        ImageView dateIV = getActivity().findViewById(R.id.iconDate);
        dateIV.setOnClickListener(v -> new DatePickerDialog(
                getContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    SimpleDateFormat dateFormat1 = getDateFormat();
                    TextView dateET = getActivity().findViewById(R.id.date);
                    dateET.setText(dateFormat1.format(myCalendar.getTime()));
                    SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(MainActivity.DATEDIARY, dateFormat2.format(myCalendar.getTime()));
                    editor.apply();
                },
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
        ).show());
    }

    //set date of session
    private void setDate(){
        pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);

        String date = pref.getString(MainActivity.DATEDIARY, null);

        if (!pref.getBoolean(MainActivity.DATEDEF,true)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
            try {
                Date dateD = format.parse(date);
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy", Locale.getDefault());
                date = dateFormat.format(dateD);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
            try {
                Date dateD = format.parse(date);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                date = dateFormat.format(dateD);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        TextView dateET = getActivity().findViewById(R.id.date);
        dateET.setText(date);
    }

    //set date pattern with preference
    private SimpleDateFormat getDateFormat(){
        pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.getBoolean(MainActivity.DATEDEF,true)) {
            return new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        } else{
            return new SimpleDateFormat("MMMM dd yyyy", Locale.getDefault());
        }
    }

    private void loadReports() {

        //find the date
        String day = getDate();

        pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);

        getActivity().findViewById(R.id.no_report_diary).setVisibility(View.VISIBLE);
        //visualize the Physical Activity reports of the day and the calories used in the day
        getActivity().findViewById(R.id.PAdiv).setVisibility(View.GONE);
        ((LinearLayout) getActivity().findViewById(R.id.PAcont)).removeAllViews();
        if (pref.getBoolean(MainActivity.PHYSICAL_ACTIVITY, true)) {
            visualizePAReports(day);
        }
        //visualize the Glycemia reports of the day and the avg value
        getActivity().findViewById(R.id.Gdiv).setVisibility(View.GONE);
        ((LinearLayout) getActivity().findViewById(R.id.Gcont)).removeAllViews();
        if (pref.getBoolean(MainActivity.GLYCEMIA, true)) {
            visualizeGReports(day);
        }
        //visualize the Weight reports of the day and the avg value
        getActivity().findViewById(R.id.Wdiv).setVisibility(View.GONE);
        ((LinearLayout) getActivity().findViewById(R.id.Wcont)).removeAllViews();
        if (pref.getBoolean(MainActivity.WEIGHT, true)) {
            visualizeWReports(day);
        }
        //visualize the Pulse reports of the day and the avg value
        getActivity().findViewById(R.id.Pdiv).setVisibility(View.GONE);
        ((LinearLayout) getActivity().findViewById(R.id.Pcont)).removeAllViews();
        if (pref.getBoolean(MainActivity.PULSE, true)) {
            visualizePReports(day);
        }
        //visualize the Oxygen reports of the day and the avg value
        getActivity().findViewById(R.id.Odiv).setVisibility(View.GONE);
        ((LinearLayout) getActivity().findViewById(R.id.Ocont)).removeAllViews();
        if (pref.getBoolean(MainActivity.OXYGEN, true)) {
            visualizeOReports(day);
        }
        //visualize the Temperature reports of the day and the avg value
        getActivity().findViewById(R.id.Tdiv).setVisibility(View.GONE);
        ((LinearLayout) getActivity().findViewById(R.id.Tcont)).removeAllViews();
        if (pref.getBoolean(MainActivity.TEMPERATURE, true)){
            visualizeTReports(day);
        }
    }

    //get date in format dd/MM/yyyy
    protected String getDate(){
        String date = ((TextView)getActivity().findViewById(R.id.date)).getText().toString();

        pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
        if (!pref.getBoolean(MainActivity.DATEDEF,true)) {
            SimpleDateFormat format = new SimpleDateFormat("MMMM dd yyyy", Locale.getDefault());
            try {
                Date dateD = format.parse(date);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                date = dateFormat.format(dateD);
                return  date;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else {
            SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            try {
                Date dateD = format.parse(date);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                date = dateFormat.format(dateD);
                return  date;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    //get Physical Activity Reports of a day from the database and visualize the value in the correct graphic form
    private void visualizePAReports(String day){
        DBHelper dbh = MainActivity.getDBHelper();

        Cursor cursor = dbh.getPhysicalActivityReportsOfDay(day);
        cursor.moveToFirst();

        int n = 0;
        int sum = 0;

        while (!cursor.isAfterLast()) {
            //extract value from cursor
            SpannableString[] value = new SpannableString[6];

            if (pref.getBoolean(MainActivity.PA_ATTENTION, true)) {
                String[] itemA = getResources().getStringArray(R.array.attention);
                int i = cursor.getInt(cursor.getColumnIndex(DBHelper.PA_ATTENTION));
                value[0] = new SpannableString(getString(R.string.attention) + ": " + itemA[i]);
                value[0].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.attention).length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            value[1] = new SpannableString(getString(R.string.calories)+": "+cursor.getString(cursor.getColumnIndex(DBHelper.PA_CALORIES))+"kcal");
            value[1].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.calories).length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            if (cursor.getString(cursor.getColumnIndex(DBHelper.PA_STEPS))!=null && pref.getBoolean(MainActivity.PA_STEPS, true)){
                value[2] = new SpannableString(getString(R.string.steps)+": "+cursor.getString(cursor.getColumnIndex(DBHelper.PA_STEPS)));
                value[2].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.steps).length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (getLengthInCorrectForm(cursor.getString(cursor.getColumnIndex(DBHelper.PA_DISTANCE)))!=null && pref.getBoolean(MainActivity.PA_DISTANCE, true)){
                value[3] = new SpannableString(getString(R.string.distance)+": "+getLengthInCorrectForm(cursor.getString(cursor.getColumnIndex(DBHelper.PA_DISTANCE))));
                value[3].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.distance).length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (cursor.getString(cursor.getColumnIndex(DBHelper.PA_DURATION))!=null && pref.getBoolean(MainActivity.PA_DURATION, true)){
                value[4] = new SpannableString(getString(R.string.duration)+": "+cursor.getString(cursor.getColumnIndex(DBHelper.PA_DURATION))+"min");
                value[4].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.duration).length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (cursor.getString(cursor.getColumnIndex(DBHelper.PA_COMMENT))!=null && pref.getBoolean(MainActivity.PA_COMMENT, true)){
                value[5] = new SpannableString(getString(R.string.comment)+": "+cursor.getString(cursor.getColumnIndex(DBHelper.PA_COMMENT)));
                value[5].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.comment).length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            visualizeReport(R.id.PAcont, n, value, cursor.getInt(cursor.getColumnIndex(DBHelper.PA_ID)));

            if (n==0){
                getActivity().findViewById(R.id.PAdiv).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.no_report_diary).setVisibility(View.GONE);
                closeReportsContainer(R.id.PAcont, R.id.openPA, R.id.closePA);
            }

            //next cursor & sum of calories in the day
            n++;
            sum = sum + cursor.getInt(cursor.getColumnIndex(DBHelper.PA_CALORIES));
            cursor.moveToNext();
        }

        //visualise sum of calories in the day
        TextView avgPA = getActivity().findViewById(R.id.avgPA);
        String s = sum +"kcal";
        avgPA.setText(s);

        cursor.close();
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
            return format(Locale.getDefault(), MainActivity.FORMAT, dist) + getResources().getString(R.string.length2);
        } else {
            return format(Locale.getDefault(), MainActivity.FORMAT, Float.parseFloat(distance))+getResources().getString(R.string.length1);
        }
    }

    //get Glycemia Reports of a day from the database and visualize the value in the correct graphic form
    private void visualizeGReports(String day){
        DBHelper dbh = MainActivity.getDBHelper();

        Cursor cursor = dbh.getGlycemiaReportsOfDay(day);
        cursor.moveToFirst();

        int n = 0;
        float sum = 0;

        while (!cursor.isAfterLast()) {
            //extract value from cursor
            SpannableString[] value = new SpannableString[8];

            if (pref.getBoolean(MainActivity.G_ATTENTION, true)) {
                String[] itemA = getResources().getStringArray(R.array.attention);
                int i = cursor.getInt(cursor.getColumnIndex(DBHelper.G_ATTENTION));
                value[0] = new SpannableString(getString(R.string.attention) + ": " + itemA[i]);
                value[0].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.attention).length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            value[1] = new SpannableString(getString(R.string.glycemiaVal)+": "+getGlycemiaInCorrectForm(cursor.getString(cursor.getColumnIndex(DBHelper.G_VALUE))));
            value[1].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.glycemiaVal).length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            if (pref.getBoolean(MainActivity.G_LABEL, true)) {
                String[] itemI = getResources().getStringArray(R.array.label);
                int j = cursor.getInt(cursor.getColumnIndex(DBHelper.G_LABEL));
                value[2] = new SpannableString(getString(R.string.label) + ": " + itemI[j]);
                value[2].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.label).length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (cursor.getString(cursor.getColumnIndex(DBHelper.G_HbA1C))!=null && pref.getBoolean(MainActivity.G_HBA1C, true)){
                value[3] = new SpannableString(getString(R.string.HbA1C)+": "+ format(Locale.getDefault(), MainActivity.FORMAT, cursor.getFloat(cursor.getColumnIndex(DBHelper.G_HbA1C)))+"%");
                value[3].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.HbA1C).length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (cursor.getString(cursor.getColumnIndex(DBHelper.G_BREAD_UNIT))!=null && pref.getBoolean(MainActivity.G_BREAD_UNIT, true)){
                value[4] = new SpannableString(getString(R.string.breadUnit)+": "+cursor.getString(cursor.getColumnIndex(DBHelper.G_BREAD_UNIT))+"BU");
                value[4].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.breadUnit).length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (cursor.getString(cursor.getColumnIndex(DBHelper.G_BOLUS))!=null && pref.getBoolean(MainActivity.G_BOLUS, true)){
                value[5] = new SpannableString(getString(R.string.bolus)+": "+ format(Locale.getDefault(), MainActivity.FORMAT, cursor.getFloat(cursor.getColumnIndex(DBHelper.G_BOLUS)))+"IU");
                value[5].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.bolus).length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (cursor.getString(cursor.getColumnIndex(DBHelper.G_BASAL))!=null && pref.getBoolean(MainActivity.G_BASAL, true)){
                value[6] = new SpannableString(getString(R.string.basal)+": "+ format(Locale.getDefault(), MainActivity.FORMAT, cursor.getFloat(cursor.getColumnIndex(DBHelper.G_BASAL)))+"%");
                value[6].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.basal).length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (cursor.getString(cursor.getColumnIndex(DBHelper.G_COMMENT))!=null && pref.getBoolean(MainActivity.G_COMMENT, true)){
                value[7] = new SpannableString(getString(R.string.comment)+": "+cursor.getString(cursor.getColumnIndex(DBHelper.G_COMMENT)));
                value[7].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.comment).length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            visualizeReport(R.id.Gcont, n, value, cursor.getInt(cursor.getColumnIndex(DBHelper.W_ID)));

            if (n==0){
                getActivity().findViewById(R.id.Gdiv).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.no_report_diary).setVisibility(View.GONE);
                closeReportsContainer(R.id.Gcont, R.id.openG, R.id.closeG);
            }

            //next cursor & sum of calories in the day
            n++;
            sum = sum + cursor.getFloat(cursor.getColumnIndex(DBHelper.G_VALUE));
            cursor.moveToNext();
        }

        //visualise sum of calories in the day
        if (n>0){
            TextView avgPA = getActivity().findViewById(R.id.avgG);
            sum = sum / n;
            avgPA.setText(getGlycemiaInCorrectForm(Float.toString(sum)));
        }

        cursor.close();
    }

    //visualize glycemia on Pref unit of measure
    private String getGlycemiaInCorrectForm(String glycemia) {
        if (glycemia==null){
            return null;
        }
        pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
        if (!pref.getBoolean(MainActivity.GLYCEMIADEF, true)) {
            float g;
            g = Float.parseFloat(glycemia);
            g = (float) (g / 18.0182);
            return format(Locale.getDefault(), MainActivity.FORMAT, g)+getResources().getString(R.string.glycemia2);
        } else {
            return format(Locale.getDefault(), MainActivity.FORMAT, Float.parseFloat(glycemia))+getResources().getString(R.string.glycemia1);
        }
    }

    //get Weight Reports of a day from the database and visualize the value in the correct graphic form
    private void visualizeWReports(String day){
        DBHelper dbh = MainActivity.getDBHelper();

        Cursor cursor = dbh.getWeightReportsOfDay(day);
        cursor.moveToFirst();

        int n = 0;
        float sum = 0;

        while (!cursor.isAfterLast()) {
            //extract value from cursor
            SpannableString[] value = new SpannableString[6];

            if (pref.getBoolean(MainActivity.W_ATTENTION, true)) {
                String[] itemA = getResources().getStringArray(R.array.attention);
                int i = cursor.getInt(cursor.getColumnIndex(DBHelper.W_ATTENTION));
                value[0] = new SpannableString(getString(R.string.attention) + ": " + itemA[i]);
                value[0].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.attention).length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            value[1] = new SpannableString(getString(R.string.weight)+": "+getWeightInCorrectForm(cursor.getString(cursor.getColumnIndex(DBHelper.W_VALUE))));
            value[1].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.weight).length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            if (cursor.getString(cursor.getColumnIndex(DBHelper.W_MUSCLES))!=null && pref.getBoolean(MainActivity.W_MUSCLES, true)){
                value[2] = new SpannableString(getString(R.string.muscles)+": "+ format(Locale.getDefault(), MainActivity.FORMAT, cursor.getFloat(cursor.getColumnIndex(DBHelper.W_MUSCLES)))+"%");
                value[2].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.muscles).length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (cursor.getString(cursor.getColumnIndex(DBHelper.W_BODY_FAT))!=null && pref.getBoolean(MainActivity.W_BODY_FAT, true)){
                value[3] = new SpannableString(getString(R.string.bodyFat)+": "+ format(Locale.getDefault(), MainActivity.FORMAT, cursor.getFloat(cursor.getColumnIndex(DBHelper.W_BODY_FAT)))+"%");
                value[3].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.bodyFat).length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (cursor.getString(cursor.getColumnIndex(DBHelper.W_WATER))!=null && pref.getBoolean(MainActivity.W_WATER, true)){
                value[4] = new SpannableString(getString(R.string.water)+": "+ format(Locale.getDefault(), MainActivity.FORMAT, cursor.getFloat(cursor.getColumnIndex(DBHelper.W_WATER)))+"%");
                value[4].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.water).length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (cursor.getString(cursor.getColumnIndex(DBHelper.W_COMMENT))!=null && pref.getBoolean(MainActivity.W_COMMENT, true)){
                value[5] = new SpannableString(getString(R.string.comment)+": "+cursor.getString(cursor.getColumnIndex(DBHelper.W_COMMENT)));
                value[5].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.comment).length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            visualizeReport(R.id.Wcont, n, value, cursor.getInt(cursor.getColumnIndex(DBHelper.W_ID)));

            if (n==0){
                getActivity().findViewById(R.id.Wdiv).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.no_report_diary).setVisibility(View.GONE);
                closeReportsContainer(R.id.Wcont, R.id.openW, R.id.closeW);
            }

            //next cursor & sum of calories in the day
            n++;
            sum = sum + cursor.getFloat(cursor.getColumnIndex(DBHelper.W_VALUE));
            cursor.moveToNext();
        }

        //visualise sum of calories in the day
        if (n>0){
            TextView avgPA = getActivity().findViewById(R.id.avgW);
            sum = sum / n;
            avgPA.setText(getWeightInCorrectForm(Float.toString(sum)));
        }

        cursor.close();
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
            return format(Locale.getDefault(), MainActivity.FORMAT, w)+getResources().getString(R.string.weight2);
        } else {
            return format(Locale.getDefault(), MainActivity.FORMAT, Float.parseFloat(weight))+getResources().getString(R.string.weight1);
        }
    }

    //get Pulse Reports of a day from the database and visualize the value in the correct graphic form
    private void visualizePReports(String day){
        DBHelper dbh = MainActivity.getDBHelper();

        Cursor cursor = dbh.getPulseReportsOfDay(day);
        cursor.moveToFirst();

        int n = 0;
        float sum = 0;

        while (!cursor.isAfterLast()) {
            //extract value from cursor
            SpannableString[] value = new SpannableString[5];

            if (pref.getBoolean(MainActivity.P_ATTENTION, true)) {
                String[] itemA = getResources().getStringArray(R.array.attention);
                int i = cursor.getInt(cursor.getColumnIndex(DBHelper.P_ATTENTION));
                value[0] = new SpannableString(getString(R.string.attention)+": "+itemA[i]);
                value[0].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.attention).length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            value[1] = new SpannableString(getString(R.string.avgPulse)+": "+cursor.getString(cursor.getColumnIndex(DBHelper.P_AVG))+"bpm");
            value[1].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.avgPulse).length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            if (cursor.getString(cursor.getColumnIndex(DBHelper.P_MAX))!=null && pref.getBoolean(MainActivity.P_MAX, true)){
                value[2] = new SpannableString(getString(R.string.maxPulse)+": "+cursor.getString(cursor.getColumnIndex(DBHelper.P_MAX))+"bpm");
                value[2].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.maxPulse).length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (cursor.getString(cursor.getColumnIndex(DBHelper.P_MIN))!=null && pref.getBoolean(MainActivity.P_MIN, true)){
                value[3] = new SpannableString(getString(R.string.minPulse)+": "+cursor.getString(cursor.getColumnIndex(DBHelper.P_MIN))+"bpm");
                value[3].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.minPulse).length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (cursor.getString(cursor.getColumnIndex(DBHelper.P_COMMENT))!=null && pref.getBoolean(MainActivity.P_COMMENT, true)){
                value[4] = new SpannableString(getString(R.string.comment)+": "+cursor.getString(cursor.getColumnIndex(DBHelper.P_COMMENT)));
                value[4].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.comment).length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            visualizeReport(R.id.Pcont, n, value, cursor.getInt(cursor.getColumnIndex(DBHelper.P_ID)));

            if (n==0){
                getActivity().findViewById(R.id.Pdiv).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.no_report_diary).setVisibility(View.GONE);
                closeReportsContainer(R.id.Pcont, R.id.openP, R.id.closeP);
            }

            //next cursor & sum of calories in the day
            n++;
            sum = sum + cursor.getFloat(cursor.getColumnIndex(DBHelper.P_AVG));
            cursor.moveToNext();
        }

        //visualise sum of calories in the day
        if (n>0){
            TextView avgPA = getActivity().findViewById(R.id.avgP);
            sum = sum / n;
            String s = sum +"bpm";
            avgPA.setText(s);
        }

        cursor.close();
    }

    //get Oxygen Reports of a day from the database and visualize the value in the correct graphic form
    private void visualizeOReports(String day){
        DBHelper dbh = MainActivity.getDBHelper();

        Cursor cursor = dbh.getOxygenReportsOfDay(day);
        cursor.moveToFirst();

        int n = 0;
        int sum = 0;

        while (!cursor.isAfterLast()) {
            //extract value from cursor
            SpannableString[] value = new SpannableString[5];

            String[] itemA = getResources().getStringArray(R.array.attention);
            if (pref.getBoolean(MainActivity.O_ATTENTION, true)) {
                int i = cursor.getInt(cursor.getColumnIndex(DBHelper.O_ATTENTION));
                value[0] = new SpannableString(getString(R.string.attention) + ": " + itemA[i]);
                value[0].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.attention).length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            value[1] = new SpannableString(getString(R.string.SpO)+": "+cursor.getString(cursor.getColumnIndex(DBHelper.O_AVG))+"%");
            value[1].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.SpO).length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            if (cursor.getString(cursor.getColumnIndex(DBHelper.O_MAX))!=null && pref.getBoolean(MainActivity.O_MAX, true)){
                value[2] = new SpannableString(getString(R.string.SpOMax)+": "+cursor.getString(cursor.getColumnIndex(DBHelper.O_MAX))+"%");
                value[2].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.SpOMax).length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (cursor.getString(cursor.getColumnIndex(DBHelper.O_MIN))!=null && pref.getBoolean(MainActivity.O_MIN, true)){
                value[3] = new SpannableString(getString(R.string.SpOMin)+": "+cursor.getString(cursor.getColumnIndex(DBHelper.O_MIN))+"%");
                value[3].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.SpOMin).length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (cursor.getString(cursor.getColumnIndex(DBHelper.O_COMMENT))!=null && pref.getBoolean(MainActivity.O_COMMENT, true)){
                value[4] = new SpannableString(getString(R.string.comment)+": "+cursor.getString(cursor.getColumnIndex(DBHelper.O_COMMENT)));
                value[4].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.comment).length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            visualizeReport(R.id.Ocont, n, value, cursor.getInt(cursor.getColumnIndex(DBHelper.O_ID)));

            if (n==0){
                getActivity().findViewById(R.id.Odiv).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.no_report_diary).setVisibility(View.GONE);
                closeReportsContainer(R.id.Ocont, R.id.openO, R.id.closeO);
            }

            //next cursor & sum of calories in the day
            n++;
            sum = sum + cursor.getInt(cursor.getColumnIndex(DBHelper.O_AVG));
            cursor.moveToNext();
        }

        //visualise sum of calories in the day
        if (n>0){
            TextView avgPA = getActivity().findViewById(R.id.avgO);
            sum = sum / n;
            String s = sum+"%";
            avgPA.setText(s);
        }

        cursor.close();
    }

    //get Temperature Reports of a day from the database and visualize the value in the correct graphic form
    private void visualizeTReports(String day){
        DBHelper dbh = MainActivity.getDBHelper();

        Cursor cursor = dbh.getTemperatureReportsOfDay(day);
        cursor.moveToFirst();

        int n = 0;
        float sum = 0;

        while (!cursor.isAfterLast()) {
            //extract value from cursor
            SpannableString[] value = new SpannableString[3];

            if (pref.getBoolean(MainActivity.T_ATTENTION, true)) {
                String[] itemA = getResources().getStringArray(R.array.attention);
                int i = cursor.getInt(cursor.getColumnIndex(DBHelper.T_ATTENTION));
                value[0] = new SpannableString(getString(R.string.attention) + ": " + itemA[i]);
                value[0].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.attention).length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            value[1] = new SpannableString(getString(R.string.temperature)+": "+getTemperatureInCorrectForm(cursor.getString(cursor.getColumnIndex(DBHelper.T_VALUE))));
            value[1].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.temperature).length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            if (cursor.getString(cursor.getColumnIndex(DBHelper.T_COMMENT))!=null && pref.getBoolean(MainActivity.T_COMMENT, true)){
                value[2] = new SpannableString(getString(R.string.comment)+": "+cursor.getString(cursor.getColumnIndex(DBHelper.T_COMMENT)));
                value[2].setSpan(new StyleSpan(Typeface.BOLD), 0, getString(R.string.comment).length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            visualizeReport(R.id.Tcont, n, value, cursor.getInt(cursor.getColumnIndex(DBHelper.T_ID)));

            if (n==0){
                getActivity().findViewById(R.id.Tdiv).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.no_report_diary).setVisibility(View.GONE);
                closeReportsContainer(R.id.Tcont, R.id.openT, R.id.closeT);
            }

            //next cursor & sum of calories in the day
            n++;
            sum = sum + cursor.getFloat(cursor.getColumnIndex(DBHelper.T_VALUE));
            cursor.moveToNext();
        }

        //visualise sum of calories in the day
        if (n>0) {
            TextView avgPA = getActivity().findViewById(R.id.avgT);
            sum = sum / n;
            avgPA.setText(getTemperatureInCorrectForm(Float.toString(sum)));
        }

        cursor.close();
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
            t = (float) (t * 1.8) + 32 ;
            return format(Locale.getDefault(), MainActivity.FORMAT, t) + getResources().getString(R.string.temperature2);
        } else {
            return format(Locale.getDefault(), MainActivity.FORMAT, Float.parseFloat(temperature))+getResources().getString(R.string.temperature1);
        }
    }

    //visualize report in UI
    private void visualizeReport(int idContainer, int n, SpannableString[] info, int idTuple){
        LinearLayout container = getActivity().findViewById(idContainer);

        if (n>0){
            //create line divisor
            View line = new View(getContext());
            LinearLayout.LayoutParams lpLine = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 4);
            line.setLayoutParams(lpLine);
            line.setBackgroundResource(R.color.green_dark);
            line.setId(View.generateViewId());

            container.addView(line);
        }

        //create the FlexBox for the info
        FlexboxLayout fl = new FlexboxLayout(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        fl.setLayoutParams(lp);
        fl.setFlexWrap(FlexWrap.WRAP);

        //create the fl id
        String idName = "FlexBox"+n;
        idMap.put(idName,View.generateViewId());
        fl.setId(idMap.get(idName));
        fl.setOnClickListener(thisView -> modify(idContainer, idTuple));

        //add a TextView for each date in info and add to fl
        for (SpannableString s: info) {
            if (s!=null) {
                TextView tv = new TextView(getContext());
                LinearLayout.LayoutParams lpTV = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lpTV.setMarginEnd(20);
                tv.setLayoutParams(lpTV);

                tv.setText(s);
                fl.addView(tv);
            }
        }

        //add fl to the container
        container.addView(fl);
    }

    //open ModifyReportActivity for modify the tuple
    private void modify(int idContainer, int idTuple) {
        final int idPhysicActivity = R.id.PAcont;
        final int idGlycemia = R.id.Gcont;
        final int idWeight = R.id.Wcont;
        final int idPulse = R.id.Pcont;
        final int idOxygen = R.id.Ocont;
        final int idTemperature = R.id.Tcont;

        Intent i = new Intent();
        i.setClass(getContext(), ModifyReportActivity.class);
        switch (idContainer){
            case idPhysicActivity:
                i.putExtra("type","PA");
                break;
            case idGlycemia:
                i.putExtra("type","G");
                break;
            case idWeight:
                i.putExtra("type","W");
                break;
            case idPulse:
                i.putExtra("type","P");
                break;
            case idOxygen:
                i.putExtra("type","O");
                break;
            case idTemperature:
                i.putExtra("type","T");
                break;
        }
        i.putExtra("id", idTuple);
        startActivity(i);
    }
}