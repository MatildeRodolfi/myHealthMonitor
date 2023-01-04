package com.example.myhealthmonitor;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class alarmsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FloatingActionButton FabAddAlarm =  getView().findViewById(R.id.new_alarm);
        FabAddAlarm.setOnClickListener(thisView -> {
            Intent i = new Intent();
            i.setClass(getContext(), NewAlarmActivity.class);
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
        ArrayList<Alarm> alarmsList = new ArrayList<>();
        RecyclerView recycler = getView().findViewById(R.id.RV_alarms);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);
        adapterAlarms listAdapter = new adapterAlarms(alarmsList, getContext());
        recycler.setAdapter(listAdapter);

        TextView noAlarm = getView().findViewById(R.id.no_alarm);
        boolean find=false;

        DBHelper dbh = MainActivity.getDBHelper();

        Cursor cursor = dbh.getAlarms();
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            find=true;
            int id = cursor.getInt(cursor.getColumnIndex(DBHelper.A_ID));
            String date = cursor.getString(cursor.getColumnIndex(DBHelper.A_DATE));
            boolean repeat = cursor.getInt(cursor.getColumnIndex(DBHelper.A_REPEAT)) == 1;
            String time = cursor.getString(cursor.getColumnIndex(DBHelper.A_TIME));
            String name = cursor.getString(cursor.getColumnIndex(DBHelper.A_NAME));
            boolean sound = cursor.getInt(cursor.getColumnIndex(DBHelper.A_SOUND)) == 1;
            boolean vibration = cursor.getInt(cursor.getColumnIndex(DBHelper.A_VIBRATION)) == 1;
            alarmsList.add(new Alarm(id, date, repeat, time, name, sound, vibration));
            cursor.moveToNext();
        }

        listAdapter.notifyDataSetChanged();

        cursor.close();
        if (find){
            noAlarm.setVisibility(View.GONE);
        } else{
            noAlarm.setVisibility(View.VISIBLE);
        }
    }
}
