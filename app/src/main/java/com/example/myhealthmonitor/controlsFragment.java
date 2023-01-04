package com.example.myhealthmonitor;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class controlsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_controls, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FloatingActionButton FabAddControl =  getView().findViewById(R.id.new_control);
        FabAddControl.setOnClickListener(thisView -> {
            Intent i = new Intent();
            i.setClass(getContext(), NewControlActivity.class);
            startActivity(i);
        });
    }

    @Override
    public void onResume() {
        loadControl();
        super.onResume();
    }

    private void loadControl(){
        //set RecyclerView data
        ArrayList<Control> contactsList = new ArrayList<>();
        RecyclerView recycler = getView().findViewById(R.id.RV_ControlParameters);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);
        adapterControls listAdapter = new adapterControls(contactsList, getContext());
        recycler.setAdapter(listAdapter);

        TextView noControl = getView().findViewById(R.id.no_control_parameters);
        boolean find=false;

        DBHelper dbh = MainActivity.getDBHelper();

        Cursor cursor = dbh.getControls();
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            find=true;
            int id = cursor.getInt(cursor.getColumnIndex(DBHelper.C_ID));
            String date = cursor.getString(cursor.getColumnIndex(DBHelper.C_DATE));
            boolean repeat = cursor.getInt(cursor.getColumnIndex(DBHelper.C_REPEAT)) == 1;
            String time = cursor.getString(cursor.getColumnIndex(DBHelper.C_TIME));
            int timing = cursor.getInt(cursor.getColumnIndex(DBHelper.C_TIMING));
            int reportType = cursor.getInt(cursor.getColumnIndex(DBHelper.C_REPORT_TYPE));
            int valueType = cursor.getInt(cursor.getColumnIndex(DBHelper.C_VALUE_TYPE));
            float min = -23;
            if (cursor.getString(cursor.getColumnIndex(DBHelper.C_MIN))!=null){
                min = cursor.getFloat(cursor.getColumnIndex(DBHelper.C_MIN));
            }
            float max=-23;
            if (cursor.getString(cursor.getColumnIndex(DBHelper.C_MAX))!=null){
                max = cursor.getFloat(cursor.getColumnIndex(DBHelper.C_MAX));
            }
            int attentionMax = cursor.getInt(cursor.getColumnIndex(DBHelper.C_MAX_ATTENTION));
            int attentionMin = cursor.getInt(cursor.getColumnIndex(DBHelper.C_MIN_ATTENTION));
            boolean sound = cursor.getInt(cursor.getColumnIndex(DBHelper.C_SOUND)) == 1;
            boolean vibration = cursor.getInt(cursor.getColumnIndex(DBHelper.C_VIBRATION)) == 1;
            contactsList.add(new Control(id, date, repeat, time, timing, reportType, valueType, max, min, attentionMax, attentionMin, sound, vibration));
            cursor.moveToNext();
        }

        listAdapter.notifyDataSetChanged();

        cursor.close();
        if (find){
            noControl.setVisibility(View.GONE);
        } else{
            noControl.setVisibility(View.VISIBLE);
        }
    }
}
