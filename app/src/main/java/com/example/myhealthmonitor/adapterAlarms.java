package com.example.myhealthmonitor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class adapterAlarms extends RecyclerView.Adapter{

    private final ArrayList<Alarm> alarmsList;
    private final Context context;

    public adapterAlarms(ArrayList<Alarm> alarmsList, Context context) {
        this.alarmsList = alarmsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_alarm, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Alarm alarm = alarmsList.get(position);
        ViewHolder vh = (ViewHolder) holder;

        vh.setItem(alarm.getDate(), alarm.getRepeat(), alarm.getTime(), alarm.getName(), alarm.getSound(), alarm.getVibration());

        vh.itemView.setOnClickListener(arg0 -> {
            Intent i = new Intent();
            i.setClass(context, ModifyAlarmActivity.class);
            i.putExtra("id", alarm.getId());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return alarmsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView timeTV;
        private final TextView nameTV;
        private final TextView dateTV;
        private final ImageView soundIV;
        private final ImageView vibrationIV;

        public ViewHolder(View itemView) {
            super(itemView);

            timeTV = itemView.findViewById(R.id.alarmTime);
            nameTV = itemView.findViewById(R.id.alarmName);
            dateTV = itemView.findViewById(R.id.alarmDate);
            soundIV = itemView.findViewById(R.id.alarmIconSound);
            vibrationIV = itemView.findViewById(R.id.alarmIconVibration);
        }

        public void setItem(String date, boolean repeat, String time, String name, boolean sound, boolean vibration) {
            SharedPreferences pref = context.getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
            if (repeat){
                String[] dateSeparated = date.split(";");
                date = "";
                int i=0;
                ArrayList<String> daysOfWeekAbbreviation = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.daysOfWeekAbbreviation)));
                if (dateSeparated[i].equals("0")){
                    date += daysOfWeekAbbreviation.get(0) + " ";
                    i++;
                }
                if (i<dateSeparated.length) {
                    if (dateSeparated[i].equals("1")) {
                        date += daysOfWeekAbbreviation.get(1) + " ";
                        i++;
                    }
                }
                if (i<dateSeparated.length) {
                    if (dateSeparated[i].equals("2")) {
                        date += daysOfWeekAbbreviation.get(2) + " ";
                        i++;
                    }
                }
                if (i<dateSeparated.length) {
                    if (dateSeparated[i].equals("3")) {
                        date += daysOfWeekAbbreviation.get(3) + " ";
                        i++;
                    }
                }
                if (i<dateSeparated.length) {
                    if (dateSeparated[i].equals("4")) {
                        date += daysOfWeekAbbreviation.get(4) + " ";
                        i++;
                    }
                }
                if (i<dateSeparated.length) {
                    if (dateSeparated[i].equals("5")) {
                        date += daysOfWeekAbbreviation.get(5) + " ";
                        i++;
                    }
                }
                if (i<dateSeparated.length) {
                    if (dateSeparated[i].equals("6")) {
                        date += daysOfWeekAbbreviation.get(6) + " ";
                    }
                }
            }
            else{
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
            }
            dateTV.setText(date);

            if (!pref.getBoolean(MainActivity.TIMEDEF,true)) {
                SimpleDateFormat format = new SimpleDateFormat("kk:mm", Locale.getDefault());
                try {
                    Date timeD = format.parse(time);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
                    time = dateFormat.format(timeD);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            timeTV.setText(time);

            nameTV.setText(name);

            if (sound){
                soundIV.setVisibility(View.VISIBLE);
            } else{
                soundIV.setVisibility(View.GONE);
            }

            if (vibration){
                vibrationIV.setVisibility(View.VISIBLE);
            } else{
                vibrationIV.setVisibility(View.GONE);
            }
        }
    }
}
