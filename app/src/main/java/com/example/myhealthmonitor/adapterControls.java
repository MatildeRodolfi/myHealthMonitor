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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class adapterControls extends RecyclerView.Adapter{

    private final ArrayList<Control> controlsList;
    private final Context context;

    public adapterControls(ArrayList<Control> controlsList, Context context) {
        this.controlsList = controlsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_control, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Control control = controlsList.get(position);
        ViewHolder vh = (ViewHolder) holder;

        vh.setItem(control.getDate(), control.getRepeat(), control.getTime(), control.getTiming(), control.getReportType(), control.getValueType(), control.getMax(), control.getMin());

        vh.itemView.setOnClickListener(arg0 -> {
            Intent i = new Intent();
            i.setClass(context, ModifyControlActivity.class);
            i.putExtra("id", control.getId());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return controlsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iconTypeIV;
        private final TextView valueTypeTV;
        private final TextView timingTV;
        private final TextView timeTV;
        private final TextView dateTV;

        public ViewHolder(View itemView) {
            super(itemView);

            iconTypeIV = itemView.findViewById(R.id.iconType);
            valueTypeTV = itemView.findViewById(R.id.controlItemValueType);
            timingTV = itemView.findViewById(R.id.controlItemTiming);
            timeTV = itemView.findViewById(R.id.controlItemTime);
            dateTV = itemView.findViewById(R.id.controlItemDate);
        }

        public void setItem(String date, boolean repeat, String time, int timing, int reportType, int valueType, float max, float min) {
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

            ArrayList<String> timingArray = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.timing)));
            timingTV.setText(timingArray.get(timing));

            String valType="";
            switch (reportType){
                case 0:
                    ArrayList<String> valueGENArray = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.valueGEN)));
                    valType = valueGENArray.get(valueType);
                    iconTypeIV.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_report));
                    break;
                case 1:
                    ArrayList<String> valuePAArray = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.valuePA)));
                    valType = valuePAArray.get(valueType);
                    iconTypeIV.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_shoe));
                    break;
                case 2:
                    ArrayList<String> valueGArray = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.valueG)));
                    valType = valueGArray.get(valueType);
                    iconTypeIV.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_drop));
                    break;
                case 3:
                    ArrayList<String> valueWArray = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.valueW)));
                    valType = valueWArray.get(valueType);
                    iconTypeIV.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_weight));
                    break;
                case 4:
                    ArrayList<String> valuePArray = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.valueP)));
                    valType = valuePArray.get(valueType);
                    iconTypeIV.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_heart_beat));
                    break;
                case 5:
                    ArrayList<String> valueOArray = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.valueO)));
                    valType = valueOArray.get(valueType);
                    iconTypeIV.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_oxygen));
                    break;
                case 6:
                    ArrayList<String> valueTArray = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.valueT)));
                    valType = valueTArray.get(valueType);
                    iconTypeIV.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_thermometer));
                    break;
            }

            String value;
            if (min!=-23 && max!=-23){
                value = min+" < "+valType+ " < "+max;
            }
            else{
                if (min!=-23){
                    value = valType+ " > "+min;
                }
                else{
                    value = valType+ " < "+max;
                }
            }
            valueTypeTV.setText(value);
        }
    }
}
