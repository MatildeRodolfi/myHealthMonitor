package com.example.myhealthmonitor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogFilterFragment extends DialogFragment {
    private View view;

    private SharedPreferences pref;

    private CheckBox PA;
    private CheckBox PAattention;
    private CheckBox calories;
    private CheckBox steps;
    private CheckBox distance;
    private CheckBox duration;
    private CheckBox PAcomment;

    private CheckBox G;
    private CheckBox Gattention;
    private CheckBox glycemiaVal;
    private CheckBox label;
    private CheckBox HbA1C;
    private CheckBox breadUnit;
    private CheckBox bolus;
    private CheckBox basal;
    private CheckBox Gcomment;

    private CheckBox W;
    private CheckBox Wattention;
    private CheckBox weightVal;
    private CheckBox muscles;
    private CheckBox bodyFat;
    private CheckBox water;
    private CheckBox Wcomment;

    private CheckBox P;
    private CheckBox Pattention;
    private CheckBox avgPulse;
    private CheckBox maxPulse;
    private CheckBox minPulse;
    private CheckBox Pcomment;

    private CheckBox O;
    private CheckBox Oattention;
    private CheckBox SpO;
    private CheckBox SpOMax;
    private CheckBox SpOMin;
    private CheckBox Ocomment;

    private CheckBox T;
    private CheckBox Tattention;
    private CheckBox temperatureVal;
    private CheckBox Tcomment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dialog_filter, container, false);
        
        setOnClick();
        setByPref();
        
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Get existing layout params for the window
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(params);
    }

    @Override
    public void onPause() {
        savePref();
        super.onPause();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                dismiss();
            }
        };
        super.onCreate(savedInstanceState);
    }
        
    private void setOnClick(){
        view.findViewById(R.id.back).setOnClickListener(view1 -> dismiss());

        PA = view.findViewById(R.id.PA);
        PAattention = view.findViewById(R.id.PAattention);
        calories = view.findViewById(R.id.calories);
        steps = view.findViewById(R.id.steps);
        distance = view.findViewById(R.id.distance);
        duration = view.findViewById(R.id.duration);
        PAcomment = view.findViewById(R.id.PAcomment);
        PA.setOnClickListener(v -> {
            if(PA.isChecked()){
                PAattention.setChecked(true);
                calories.setChecked(true);
                steps.setChecked(true);
                distance.setChecked(true);
                duration.setChecked(true);
                PAcomment.setChecked(true);
            } else {
                PAattention.setChecked(false);
                calories.setChecked(false);
                steps.setChecked(false);
                distance.setChecked(false);
                duration.setChecked(false);
                PAcomment.setChecked(false);
            }
        });
        PAattention.setOnClickListener(v -> onClickValuePA());
        steps.setOnClickListener(v -> onClickValuePA());
        distance.setOnClickListener(v -> onClickValuePA());
        duration.setOnClickListener(v -> onClickValuePA());
        PAcomment.setOnClickListener(v -> onClickValuePA());

        G = view.findViewById(R.id.G);
        Gattention = view.findViewById(R.id.Gattention);
        glycemiaVal = view.findViewById(R.id.glycemiaVal);
        label = view.findViewById(R.id.label);
        HbA1C = view.findViewById(R.id.HbA1C);
        breadUnit = view.findViewById(R.id.breadUnit);
        bolus = view.findViewById(R.id.bolus);
        basal = view.findViewById(R.id.basal);
        Gcomment = view.findViewById(R.id.Gcomment);
        G.setOnClickListener(v -> {
            if(G.isChecked()){
                Gattention.setChecked(true);
                glycemiaVal.setChecked(true);
                label.setChecked(true);
                HbA1C.setChecked(true);
                breadUnit.setChecked(true);
                bolus.setChecked(true);
                basal.setChecked(true);
                Gcomment.setChecked(true);
            } else {
                Gattention.setChecked(false);
                glycemiaVal.setChecked(false);
                label.setChecked(false);
                HbA1C.setChecked(false);
                breadUnit.setChecked(false);
                bolus.setChecked(false);
                basal.setChecked(false);
                Gcomment.setChecked(false);
            }
        });
        Gattention.setOnClickListener(v -> onClickValueG());
        label.setOnClickListener(v -> onClickValueG());
        HbA1C.setOnClickListener(v -> onClickValueG());
        breadUnit.setOnClickListener(v -> onClickValueG());
        bolus.setOnClickListener(v -> onClickValueG());
        basal.setOnClickListener(v -> onClickValueG());
        Gcomment.setOnClickListener(v -> onClickValueG());

        W = view.findViewById(R.id.W);
        Wattention = view.findViewById(R.id.Wattention);
        weightVal = view.findViewById(R.id.weightVal);
        muscles = view.findViewById(R.id.muscles);
        bodyFat = view.findViewById(R.id.bodyFat);
        water = view.findViewById(R.id.water);
        Wcomment = view.findViewById(R.id.Wcomment);
        W.setOnClickListener(v -> {
            if(W.isChecked()){
                Wattention.setChecked(true);
                weightVal.setChecked(true);
                muscles.setChecked(true);
                bodyFat.setChecked(true);
                water.setChecked(true);
                Wcomment.setChecked(true);
            } else {
                Wattention.setChecked(false);
                weightVal.setChecked(false);
                muscles.setChecked(false);
                bodyFat.setChecked(false);
                water.setChecked(false);
                Wcomment.setChecked(false);
            }
        });
        Wattention.setOnClickListener(v -> onClickValueW());
        muscles.setOnClickListener(v -> onClickValueW());
        bodyFat.setOnClickListener(v -> onClickValueW());
        water.setOnClickListener(v -> onClickValueW());
        Wcomment.setOnClickListener(v -> onClickValueW());

        P = view.findViewById(R.id.P);
        Pattention = view.findViewById(R.id.Pattention);
        avgPulse = view.findViewById(R.id.avgPulse);
        maxPulse = view.findViewById(R.id.maxPulse);
        minPulse = view.findViewById(R.id.minPulse);
        Pcomment = view.findViewById(R.id.Pcomment);
        P.setOnClickListener(v -> {
            if(P.isChecked()){
                Pattention.setChecked(true);
                avgPulse.setChecked(true);
                maxPulse.setChecked(true);
                minPulse.setChecked(true);
                Pcomment.setChecked(true);
            } else {
                Pattention.setChecked(false);
                avgPulse.setChecked(false);
                maxPulse.setChecked(false);
                minPulse.setChecked(false);
                Pcomment.setChecked(false);
            }
        });
        Pattention.setOnClickListener(v -> onClickValueP());
        maxPulse.setOnClickListener(v -> onClickValueP());
        minPulse.setOnClickListener(v -> onClickValueP());
        Pcomment.setOnClickListener(v -> onClickValueP());

        O = view.findViewById(R.id.O);
        Oattention = view.findViewById(R.id.Oattention);
        SpO = view.findViewById(R.id.SpO);
        SpOMax = view.findViewById(R.id.SpOMax);
        SpOMin = view.findViewById(R.id.SpOMin);
        Ocomment = view.findViewById(R.id.Ocomment);
        O.setOnClickListener(v -> {
            if(O.isChecked()){
                Oattention.setChecked(true);
                SpO.setChecked(true);
                SpOMax.setChecked(true);
                SpOMin.setChecked(true);
                Ocomment.setChecked(true);
            } else {
                Oattention.setChecked(false);
                SpO.setChecked(false);
                SpOMax.setChecked(false);
                SpOMin.setChecked(false);
                Ocomment.setChecked(false);
            }
        });
        Oattention.setOnClickListener(v -> onClickValueO());
        SpOMax.setOnClickListener(v -> onClickValueO());
        SpOMin.setOnClickListener(v -> onClickValueO());
        Ocomment.setOnClickListener(v -> onClickValueO());

        T = view.findViewById(R.id.T);
        Tattention = view.findViewById(R.id.Tattention);
        temperatureVal = view.findViewById(R.id.temperatureVal);
        Tcomment = view.findViewById(R.id.Tcomment);
        T.setOnClickListener(v -> {
            if(T.isChecked()){
                Tattention.setChecked(true);
                temperatureVal.setChecked(true);
                Tcomment.setChecked(true);
            } else {
                Tattention.setChecked(false);
                temperatureVal.setChecked(false);
                Tcomment.setChecked(false);
            }
        });
        Tattention.setOnClickListener(v -> onClickValueT());
        Tcomment.setOnClickListener(v -> onClickValueT());
    }

    private void onClickValuePA(){
        if (!PA.isChecked()){
            PA.setChecked(true);
            calories.setChecked(true);
        }
    }

    private void onClickValueG(){
        if (!G.isChecked()){
            G.setChecked(true);
            glycemiaVal.setChecked(true);
        }
    }

    private void onClickValueW(){
        if (!W.isChecked()){
            W.setChecked(true);
            weightVal.setChecked(true);
        }
    }

    private void onClickValueP(){
        if (!P.isChecked()){
            P.setChecked(true);
            avgPulse.setChecked(true);
        }
    }

    private void onClickValueO(){
        if (!O.isChecked()){
            O.setChecked(true);
            SpO.setChecked(true);
        }
    }

    private void onClickValueT(){
        if (!T.isChecked()){
            T.setChecked(true);
            temperatureVal.setChecked(true);
        }
    }

    //set items with value in pref
    private void setByPref(){
        pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);

        PA.setChecked(pref.getBoolean(MainActivity.PHYSICAL_ACTIVITY, true));
        PAattention.setChecked(pref.getBoolean(MainActivity.PA_ATTENTION, true));
        calories.setChecked(pref.getBoolean(MainActivity.PHYSICAL_ACTIVITY, true));
        steps.setChecked(pref.getBoolean(MainActivity.PA_STEPS, true));
        distance.setChecked(pref.getBoolean(MainActivity.PA_DISTANCE, true));
        duration.setChecked(pref.getBoolean(MainActivity.PA_DURATION, true));
        PAcomment.setChecked(pref.getBoolean(MainActivity.PA_COMMENT, true));

        G.setChecked(pref.getBoolean(MainActivity.GLYCEMIA, true));
        Gattention.setChecked(pref.getBoolean(MainActivity.G_ATTENTION, true));
        glycemiaVal.setChecked(pref.getBoolean(MainActivity.GLYCEMIA, true));
        label.setChecked(pref.getBoolean(MainActivity.G_LABEL, true));
        HbA1C.setChecked(pref.getBoolean(MainActivity.G_HBA1C, true));
        breadUnit.setChecked(pref.getBoolean(MainActivity.G_BREAD_UNIT, true));
        bolus.setChecked(pref.getBoolean(MainActivity.G_BOLUS, true));
        basal.setChecked(pref.getBoolean(MainActivity.G_BASAL, true));
        Gcomment.setChecked(pref.getBoolean(MainActivity.G_COMMENT, true));

        W.setChecked(pref.getBoolean(MainActivity.WEIGHT, true));
        Wattention.setChecked(pref.getBoolean(MainActivity.W_ATTENTION, true));
        weightVal.setChecked(pref.getBoolean(MainActivity.WEIGHT, true));
        muscles.setChecked(pref.getBoolean(MainActivity.W_MUSCLES, true));
        bodyFat.setChecked(pref.getBoolean(MainActivity.W_BODY_FAT, true));
        water.setChecked(pref.getBoolean(MainActivity.W_WATER, true));
        Wcomment.setChecked(pref.getBoolean(MainActivity.W_COMMENT, true));

        P.setChecked(pref.getBoolean(MainActivity.PULSE, true));
        Pattention.setChecked(pref.getBoolean(MainActivity.P_ATTENTION, true));
        avgPulse.setChecked(pref.getBoolean(MainActivity.PULSE, true));
        maxPulse.setChecked(pref.getBoolean(MainActivity.P_MAX, true));
        minPulse.setChecked(pref.getBoolean(MainActivity.P_MIN, true));
        Pcomment.setChecked(pref.getBoolean(MainActivity.P_COMMENT, true));

        O.setChecked(pref.getBoolean(MainActivity.OXYGEN, true));
        Oattention.setChecked(pref.getBoolean(MainActivity.O_ATTENTION, true));
        SpO.setChecked(pref.getBoolean(MainActivity.OXYGEN, true));
        SpOMax.setChecked(pref.getBoolean(MainActivity.O_MAX, true));
        SpOMin.setChecked(pref.getBoolean(MainActivity.O_MIN, true));
        Ocomment.setChecked(pref.getBoolean(MainActivity.O_COMMENT, true));

        T.setChecked(pref.getBoolean(MainActivity.TEMPERATURE, true));
        Tattention.setChecked(pref.getBoolean(MainActivity.T_ATTENTION, true));
        temperatureVal.setChecked(pref.getBoolean(MainActivity.TEMPERATURE, true));
        Tcomment.setChecked(pref.getBoolean(MainActivity.T_COMMENT, true));
    }

    //save value of items in pref
    private void savePref(){
        pref = getActivity().getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(MainActivity.PHYSICAL_ACTIVITY, PA.isChecked());
        editor.putBoolean(MainActivity.PA_ATTENTION, PAattention.isChecked());
        editor.putBoolean(MainActivity.PA_STEPS, steps.isChecked());
        editor.putBoolean(MainActivity.PA_DISTANCE, distance.isChecked());
        editor.putBoolean(MainActivity.PA_DURATION, duration.isChecked());
        editor.putBoolean(MainActivity.PA_COMMENT, PAcomment.isChecked());

        editor.putBoolean(MainActivity.GLYCEMIA, G.isChecked());
        editor.putBoolean(MainActivity.G_ATTENTION, Gattention.isChecked());
        editor.putBoolean(MainActivity.G_LABEL, label.isChecked());
        editor.putBoolean(MainActivity.G_HBA1C, HbA1C.isChecked());
        editor.putBoolean(MainActivity.G_BREAD_UNIT, breadUnit.isChecked());
        editor.putBoolean(MainActivity.G_BOLUS, bolus.isChecked());
        editor.putBoolean(MainActivity.G_BASAL, basal.isChecked());
        editor.putBoolean(MainActivity.G_COMMENT, Gcomment.isChecked());

        editor.putBoolean(MainActivity.WEIGHT, W.isChecked());
        editor.putBoolean(MainActivity.W_ATTENTION, Wattention.isChecked());
        editor.putBoolean(MainActivity.W_MUSCLES, muscles.isChecked());
        editor.putBoolean(MainActivity.W_BODY_FAT, bodyFat.isChecked());
        editor.putBoolean(MainActivity.W_WATER, water.isChecked());
        editor.putBoolean(MainActivity.W_COMMENT, Wcomment.isChecked());

        editor.putBoolean(MainActivity.PULSE, P.isChecked());
        editor.putBoolean(MainActivity.P_ATTENTION, Pattention.isChecked());
        editor.putBoolean(MainActivity.P_MAX, maxPulse.isChecked());
        editor.putBoolean(MainActivity.P_MIN, minPulse.isChecked());
        editor.putBoolean(MainActivity.P_COMMENT, Pcomment.isChecked());

        editor.putBoolean(MainActivity.OXYGEN, O.isChecked());
        editor.putBoolean(MainActivity.O_ATTENTION, Oattention.isChecked());
        editor.putBoolean(MainActivity.O_MAX, SpOMax.isChecked());
        editor.putBoolean(MainActivity.O_MIN, SpOMin.isChecked());
        editor.putBoolean(MainActivity.O_COMMENT, Ocomment.isChecked());

        editor.putBoolean(MainActivity.TEMPERATURE, T.isChecked());
        editor.putBoolean(MainActivity.T_ATTENTION, Tattention.isChecked());
        editor.putBoolean(MainActivity.T_COMMENT, Tcomment.isChecked());

        editor.apply();

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frame, new diaryFragment(), "Diary").commit();
    }
}
