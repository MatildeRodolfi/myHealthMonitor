package com.example.myhealthmonitor;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.ImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import static java.lang.String.format;


public class ChartLineMinMaxAvg {

    private SharedPreferences pref;

    private final Canvas canvas;
    private final Paint paint;
    private final Context context;
    private float x;
    private float y;
    private int max;

    private final int width;
    private final int height;
    private final int widthMin;
    float maxVal;

    public ChartLineMinMaxAvg(ImageView container, Context context){
        this.context = context;
        width = context.getResources().getDisplayMetrics().widthPixels;
        height = width/2;
        widthMin = (int)(width*0.05);

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        //create base of the chart
        canvas = new Canvas(bmp);
        canvas.drawColor(Color.WHITE);

        paint = new Paint();
        paint.setTypeface(ResourcesCompat.getFont(context, R.font.louis_george_cafe));
        paint.setStrokeWidth(3);
        paint.setColor(Color.BLACK);

        canvas.drawLine(widthMin, height-widthMin, width-widthMin, height-widthMin, paint);
        canvas.drawLine(width-widthMin, height-widthMin, width-(widthMin*6F/5), height-(widthMin*4F/5), paint);
        canvas.drawLine(width-widthMin, height-widthMin, width-(widthMin*6F/5), height-(widthMin*6F/5), paint);

        canvas.drawLine(widthMin, height-widthMin, widthMin, 0, paint);
        canvas.drawLine(widthMin, 0, widthMin*4F/5, widthMin*1F/5, paint);
        canvas.drawLine(widthMin, 0, widthMin*6F/5, widthMin*1F/5, paint);

        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(2);
        canvas.drawLine(widthMin, widthMin*8, width-widthMin, widthMin*8, paint);
        canvas.drawLine(widthMin, widthMin*7, width-widthMin, widthMin*7, paint);
        canvas.drawLine(widthMin, widthMin*6, width-widthMin, widthMin*6, paint);
        canvas.drawLine(widthMin, widthMin*5, width-widthMin, widthMin*5, paint);
        canvas.drawLine(widthMin, widthMin*4, width-widthMin, widthMin*4, paint);
        canvas.drawLine(widthMin, widthMin*3, width-widthMin, widthMin*3, paint);
        canvas.drawLine(widthMin, widthMin*2, width-widthMin, widthMin*2, paint);
        canvas.drawLine(widthMin, widthMin, width-widthMin, widthMin, paint);

        container.setImageBitmap(bmp);
    }

    public void setData(Cursor cursor, String ColumnIndexMin, String ColumnIndexMax, String ColumnIndexAvg, String firstDay, String lastDay){
        maxVal = 0;
        ContentValues valuesMin = extractData(cursor, ColumnIndexMin, firstDay, lastDay);
        ContentValues valuesMax = extractData(cursor, ColumnIndexMax, firstDay, lastDay);
        ContentValues valuesAvg = extractData(cursor, ColumnIndexAvg, firstDay, lastDay);

        setTiming(firstDay, lastDay);
        setUnit(maxVal);

        visualizeData(new ContentValues[]{valuesMin, valuesMax, valuesAvg}, firstDay, lastDay);
    }

    private void visualizeData(ContentValues[] values, String firstDay, String lastDay){
        for (int j=0; j<3; j++){
            System.out.println("Value groups: " + values[j]);
            Set<String> list = values[j].keySet();
            int i=0;
            float yPrev=0;
            float xPrev=0;
            List<String> keys = new ArrayList<>(list);
            Collections.sort(keys);
            for (String key:keys) {
                i++;

                //calc day between this day and the first
                long n=0;

                if (!firstDay.equals(lastDay)){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                    try {
                        Date keyDate = sdf.parse(key);
                        Date startDateValue = sdf.parse(firstDay);
                        long diff = keyDate.getTime() - startDateValue.getTime();
                        n = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+1;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    SimpleDateFormat sdf = new SimpleDateFormat("kk:mm", Locale.getDefault());
                    try {
                        Date keyDate = sdf.parse(key);
                        Date startDateValue = sdf.parse("00:00");
                        long diff = keyDate.getTime() - startDateValue.getTime();
                        n = TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                //val and transaction of the value on the chart
                if (j==0){
                    paint.setColor(ContextCompat.getColor(context, R.color.green_light));
                    paint.setStrokeWidth(4);
                }
                else{
                    if (j==1){
                        paint.setColor(ContextCompat.getColor(context, R.color.green_dark));
                        paint.setStrokeWidth(4);
                    }
                    else{
                        paint.setColor(ContextCompat.getColor(context, R.color.green));
                        paint.setStrokeWidth(6);
                    }
                }
                paint.setStyle(Paint.Style.FILL);
                Float val = values[j].getAsFloat(key);
                System.out.println("N° di sequenza: "+n+", val: "+val);
                canvas.drawCircle(widthMin+n*x, height-widthMin-val*y, widthMin/5F, paint);
                if (i!=1){
                    canvas.drawLine(xPrev, yPrev, widthMin+n*x, height-widthMin-val*y, paint);
                }
                yPrev=height-widthMin-val*y;
                xPrev=widthMin+n*x;
            }
        }


    }

    private ContentValues extractData(Cursor cursor, String ColumnIndex, String firstDay, String lastDay){
        ContentValues valCV = new ContentValues();

        cursor.moveToFirst();
        String keyIndex="date";

        if (firstDay.equals(lastDay)){
            keyIndex="time";
        }

        while (!cursor.isAfterLast()) {
            String key = cursor.getString(cursor.getColumnIndex(keyIndex));
            if (cursor.getString(cursor.getColumnIndex(ColumnIndex))!=null) {
                float val = cursor.getFloat(cursor.getColumnIndex(ColumnIndex));

                if (ColumnIndex.equals(DBHelper.PA_DISTANCE)) {
                    val = getLengthInCorrectForm(val);
                }
                if (ColumnIndex.equals(DBHelper.G_VALUE)) {
                    val = getGlycemiaInCorrectForm(val);
                }
                if (ColumnIndex.equals(DBHelper.W_VALUE)) {
                    val = getWeightInCorrectForm(val);
                }
                if (ColumnIndex.equals(DBHelper.T_VALUE)) {
                    val = getTemperatureInCorrectForm(val);
                }

                if (valCV.get(key) == null) {
                    valCV.put(key, val);
                } else {
                    String i = valCV.getAsString(key) + ";" + val;
                    valCV.put(key, i);
                }
            }
            cursor.moveToNext();
        }


        //cal avg value for each key
        Set<String> keys = valCV.keySet();

        for (String key:keys){
            String[] separatedVal = valCV.getAsString(key).split(";");
            float sum=0;
            int n=0;
            for(String val:separatedVal){
                sum=sum+Float.parseFloat(val);
                n++;
            }

            if (ColumnIndex.equals(DBHelper.PA_CALORIES) || ColumnIndex.equals(DBHelper.PA_STEPS) || ColumnIndex.equals(DBHelper.PA_DISTANCE) || ColumnIndex.equals(DBHelper.PA_DURATION)){
                //in case we represent calories we want sum not avg of the day
                if (sum>maxVal){
                    maxVal=sum;
                }
                String s = format(Locale.US, MainActivity.FORMAT, sum);
                valCV.put(key, s);
            }
            else{
                float a = sum/n;
                if (a>maxVal){
                    maxVal=a;
                }
                String avg = format(Locale.US, MainActivity.FORMAT, a);
                valCV.put(key, avg);
            }
        }

        return valCV;
    }

    private void setTiming(String firstDay, String lastDay){
        long nDay;

        if (firstDay.equals(lastDay)){
            x=(width-2*widthMin)/1440F;
            paint.setColor(Color.BLACK);
            paint.setTextSize(widthMin/2F);
            paint.setStrokeWidth(3);
            for (int i=1; i<24; i++){
                float a = widthMin + width * 0.9F / 24 * i;
                if (i%3==0) {
                    canvas.drawLine(a, height-widthMin, a, height-(widthMin*35F/50), paint);
                    String time;
                    SimpleDateFormat timeFormat;
                    pref = context.getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
                    if (pref.getBoolean(MainActivity.TIMEDEF,true)) {
                        timeFormat = new SimpleDateFormat("kk:mm", Locale.getDefault());
                    } else {
                        timeFormat = new SimpleDateFormat("hh aa", Locale.getDefault());
                    }
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("kk:mm", Locale.getDefault());
                    try {
                        time = timeFormat.format(simpleDateFormat.parse(i + ":00"));
                        float bounds = paint.measureText(time, 0, time.length());
                        canvas.drawText(time, a - bounds/2, height-(widthMin*15F/50), paint);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    canvas.drawLine(a, height-widthMin, a, height-(widthMin*4F/5), paint);
                }
            }
        }
        else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
            Date startDateValue=null;
            Date endDateValue=null;
            try {
                startDateValue = sdf.parse(firstDay);
                endDateValue = sdf.parse(lastDay);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long diff = endDateValue.getTime() - startDateValue.getTime();
            nDay = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            System.out.println("N day: "+nDay);
            x=((float)width-widthMin*2)/(nDay+2);

            paint.setColor(Color.BLACK);
            paint.setTextSize(widthMin/2F);
            paint.setStrokeWidth(3);
            int v=1;
            if (nDay>9){
                v = 1+(int)nDay/10;
            }
            Date day = startDateValue;
            for (int i=1; i<nDay+2; i++){
                SimpleDateFormat dateFormat;
                pref = context.getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
                if (pref.getBoolean(MainActivity.DATEDEF,true)) {
                    dateFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
                } else {
                    dateFormat = new SimpleDateFormat("MM/dd", Locale.getDefault());
                }

                String date = dateFormat.format(day);

                if (i%v==1 || v==1) {
                    canvas.drawLine(widthMin + x * i, height-widthMin, widthMin + x * i, height-(widthMin*35F/50), paint);
                    float bounds = paint.measureText(date, 0, date.length());
                    canvas.drawText(date, widthMin + x * i - bounds/2, height-(widthMin*15F/50), paint);
                }
                else{
                    canvas.drawLine(widthMin + x * i, height-widthMin, widthMin + x * i, height-(widthMin*4F/5), paint);
                }

                Calendar c = Calendar.getInstance();
                c.setTime(day);
                c.add(Calendar.DATE, 1);
                day = c.getTime();
            }
        }
    }

    private void setUnit(Float maxVal){
        int c=9;
        int i=1;
        while (c-i<maxVal) {
            c=c+9;
            i++;
        }
        y=(float)(height-widthMin)/c;
        paint.setColor(Color.BLACK);
        paint.setTextSize(widthMin/2F);
        paint.setStrokeWidth(3);
        for (int j=1; j<9; j++){
            String txt=Integer.toString((i*j));
            float bounds = paint.measureText(txt, 0, txt.length());
            canvas.drawText(txt, (widthMin*45F/50)-bounds, (height-widthMin*4F/5)-widthMin*j, paint);
        }
    }

    //visualize length on Pref unit of measure
    private float getLengthInCorrectForm(float distance) {
        pref = context.getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
        if (!pref.getBoolean(MainActivity.LENGTHEDEF, true)) {
            distance = (float) (distance / 1.609344);
        }
        return distance;
    }

    //visualize glycemia on Pref unit of measure
    private float getGlycemiaInCorrectForm(float glycemia) {
       pref = context.getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
        if (!pref.getBoolean(MainActivity.GLYCEMIADEF, true)) {
            glycemia = (float) (glycemia / 18.0182);
        }
        return glycemia;
    }

    //visualize weight on Pref unit of measure
    private float getWeightInCorrectForm(float weight) {
        pref = context.getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
        if (!pref.getBoolean(MainActivity.WEIGHTDEF, true)) {
            weight = (float) (weight * 2.2046);
        }
        return weight;
    }

    //visualize temperature on Pref unit of measure
    private float getTemperatureInCorrectForm(float temperature) {
        pref = context.getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
        if (!pref.getBoolean(MainActivity.TEMPERATUREDEF, true)) {
            temperature = (float) (temperature * 1.8) + 32 ;
        }
        return temperature;
    }
}
