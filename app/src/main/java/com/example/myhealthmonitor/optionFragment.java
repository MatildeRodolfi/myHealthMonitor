package com.example.myhealthmonitor;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class optionFragment extends Fragment {

    private View view;

    private SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //set unit measure by preference
        view = inflater.inflate(R.layout.fragment_option, container, false);
        setByPref();
        return view;
    }

    @Override
    public void onPause() {
        savePref();
        super.onPause();
    }

    //set items with value in pref
    private void setByPref(){
        pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
        RadioButton glycemia1 = view.findViewById(R.id.glycemia1);
        RadioButton glycemia2 = view.findViewById(R.id.glycemia2);
        if (pref.getBoolean(MainActivity.GLYCEMIADEF,true)){
            glycemia1.setChecked(true);
        } else{
            glycemia2.setChecked(true);
        }

        RadioButton weight1 = view.findViewById(R.id.weight1);
        RadioButton weight2 = view.findViewById(R.id.weight2);
        if (pref.getBoolean(MainActivity.WEIGHTDEF,true)){
            weight1.setChecked(true);
        } else{
            weight2.setChecked(true);
        }

        RadioButton temperature1 = view.findViewById(R.id.temperature1);
        RadioButton temperature2 = view.findViewById(R.id.temperature2);
        if (pref.getBoolean(MainActivity.TEMPERATUREDEF,true)){
            temperature1.setChecked(true);
        } else{
            temperature2.setChecked(true);
        }

        RadioButton length1 = view.findViewById(R.id.length1);
        RadioButton length2 = view.findViewById(R.id.length2);
        if (pref.getBoolean(MainActivity.LENGTHEDEF,true)){
            length1.setChecked(true);
        } else{
            length2.setChecked(true);
        }

        RadioButton hours1 = view.findViewById(R.id.time1);
        RadioButton hours2 = view.findViewById(R.id.time2);
        if (pref.getBoolean(MainActivity.TIMEDEF,true)){
            hours1.setChecked(true);
        } else{
            hours2.setChecked(true);
        }

        RadioButton date1 = view.findViewById(R.id.date1);
        RadioButton date2 = view.findViewById(R.id.date2);
        if (pref.getBoolean(MainActivity.DATEDEF,true)){
            date1.setChecked(true);
        } else{
            date2.setChecked(true);
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            glycemia1.setBackgroundResource(R.color.control_switch_background_selector);
            glycemia2.setBackgroundResource(R.color.control_switch_background_selector);
            weight1.setBackgroundResource(R.color.control_switch_background_selector);
            weight2.setBackgroundResource(R.color.control_switch_background_selector);
            temperature1.setBackgroundResource(R.color.control_switch_background_selector);
            temperature2.setBackgroundResource(R.color.control_switch_background_selector);
            length1.setBackgroundResource(R.color.control_switch_background_selector);
            length2.setBackgroundResource(R.color.control_switch_background_selector);
            hours1.setBackgroundResource(R.color.control_switch_background_selector);
            hours2.setBackgroundResource(R.color.control_switch_background_selector);
            date1.setBackgroundResource(R.color.control_switch_background_selector);
            date2.setBackgroundResource(R.color.control_switch_background_selector);
        }
    }

    //save value of items in pref
    private void savePref(){
        pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        RadioGroup radioGroup = view.findViewById(R.id.glycemiaRG);
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId != -1) {
            editor.putBoolean(MainActivity.GLYCEMIADEF, selectedRadioButtonId == R.id.glycemia1);
        } else {
            editor.putBoolean(MainActivity.GLYCEMIADEF, true);
        }

        radioGroup = view.findViewById(R.id.weightRG);
        selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId != -1) {
            editor.putBoolean(MainActivity.WEIGHTDEF, selectedRadioButtonId == R.id.weight1);
        } else {
            editor.putBoolean(MainActivity.WEIGHTDEF, true);
        }

        radioGroup = view.findViewById(R.id.temperatureRG);
        selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId != -1) {
            editor.putBoolean(MainActivity.TEMPERATUREDEF, selectedRadioButtonId == R.id.temperature1);
        } else {
            editor.putBoolean(MainActivity.TEMPERATUREDEF, true);
        }

        radioGroup = view.findViewById(R.id.lengthRG);
        selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId != -1) {
            editor.putBoolean(MainActivity.LENGTHEDEF, selectedRadioButtonId == R.id.length1);
        } else {
            editor.putBoolean(MainActivity.LENGTHEDEF, true);
        }

        radioGroup = view.findViewById(R.id.timeRG);
        selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId != -1) {
            editor.putBoolean(MainActivity.TIMEDEF, selectedRadioButtonId == R.id.time1);
        } else {
            editor.putBoolean(MainActivity.TIMEDEF, true);
        }

        radioGroup = view.findViewById(R.id.dateRG);
        selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId != -1) {
            editor.putBoolean(MainActivity.DATEDEF, selectedRadioButtonId == R.id.date1);
        } else {
            editor.putBoolean(MainActivity.DATEDEF, true);
        }

        editor.apply();
    }
}