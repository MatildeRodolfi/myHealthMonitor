package com.example.myhealthmonitor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.widget.ImageView;

import java.util.Locale;
import java.util.Random;
import java.util.Set;

import androidx.core.content.res.ResourcesCompat;

import static java.lang.String.format;

public class ChartPie {

    private final Canvas canvas;
    private final Paint paint;
    private final Context context;
    private final RectF oval;
    private int nVal;

    private static final String FORMAT = "%.2f";

    private final int width;
    private final int widthMin;

    public ChartPie(ImageView conteiner, Context context){
        this.context = context;
        width = context.getResources().getDisplayMetrics().widthPixels;
        widthMin = (int)(width*0.05);

        Bitmap bmp = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);

        //create
        canvas=new Canvas(bmp);
        canvas.drawColor(Color.WHITE);

        paint=new Paint();
        paint.setTypeface(ResourcesCompat.getFont(context, R.font.louis_george_cafe));

        oval = new RectF(widthMin*2, widthMin,width-widthMin*2,width-widthMin*3);

        conteiner.setImageBitmap(bmp);
    }

    public void setData(Cursor cursor, String ColumnIndex){
        ContentValues values = extractData(cursor, ColumnIndex);

        System.out.println("Value groups: " + values);

        visualizeData(values, ColumnIndex);
    }

    private void visualizeData(ContentValues values, String ColumnIndex){
        String[] label = context.getResources().getStringArray(R.array.label);
        paint.setAntiAlias(true);
        Set<String> keys = values.keySet();
        float angleStart=0;
        float startDesc=widthMin;
        boolean haveValue = false;
        for (String key:keys){
            //calc color, angle and percent
            Random rnd = new Random();
            float angle = (values.getAsFloat(key)/nVal)*360;
            float p = (values.getAsFloat(key)/nVal)*100;
            if (p>0){
                haveValue = true;
            }

            //draw the arc with the correct angle
            paint.setARGB(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawArc(oval, angleStart, angle, true, paint);

            //draw the description
            RectF square = new RectF(startDesc, width-widthMin, startDesc+widthMin/2F,width-widthMin/2F);
            canvas.drawRect(square, paint);
            paint.setColor(Color.BLACK);
            paint.setTextSize(widthMin*0.55F);
            String txt;
            if (ColumnIndex.equals(DBHelper.G_LABEL)){
                txt=label[Integer.parseInt(key)];
            }
            else{
                txt=key;
            }
            int i=0;
            float max=0F;
            if (ColumnIndex.equals("")){
                for (String line: txt.split(" ")) {
                    float bounds = paint.measureText(line, 0, line.length());
                    if (bounds>max){
                        max=bounds;
                    }
                    canvas.drawText(line, startDesc+widthMin*3F/5, width-widthMin/2F+i*widthMin*2F/5, paint);
                    i++;
                }
                startDesc = startDesc+widthMin+max;
            }
            else{
                canvas.drawText(txt, startDesc+widthMin*3F/5, width-widthMin/2F, paint);
                startDesc = startDesc+widthMin+paint.measureText(txt, 0, txt.length());
            }

            System.out.println("angle: "+angle);
            if (angle<360F && angle!=0){
                //draw border of the arc
                paint.setColor(Color.WHITE);
                paint.setStrokeWidth(10);
                paint.setStyle(Paint.Style.STROKE);
                Path path = new Path();
                path.addArc(oval, angleStart, angle);
                path.lineTo(width/2F, width/2F-widthMin);
                path.close();
                canvas.drawPath(path,paint);
            }


            //add the percentage of the arc on tot
            if (angle!=0) {
                float a = angleStart + (angle / 2);
                paint.setTextSize(widthMin*0.55F);
                paint.setStyle(Paint.Style.FILL);
                float x;
                float y;
                if (angle < 20) {
                    x = width*0.455F + width*0.450F * (float) Math.cos(Math.toRadians(a));
                    y = width*0.475F + width*0.450F * (float) Math.sin(Math.toRadians(a));
                    paint.setColor(Color.BLACK);
                } else {
                    x = width*0.450F + width*0.325F * (float) Math.cos(Math.toRadians(a));
                    y = width*0.460F + width*0.325F * (float) Math.sin(Math.toRadians(a));
                    paint.setColor(Color.WHITE);
                }
                String per = format(Locale.getDefault(), FORMAT, p) + "%";
                canvas.drawText(per, x, y, paint);
            }

            //setup for the next arc
            angleStart=angleStart+angle;
        }
        if (!haveValue){
            paint.setColor(Color.RED);
            paint.setTextSize(widthMin/2F);
            paint.setStyle(Paint.Style.FILL);
            String txt = "ERROR: no data";
            canvas.drawText(txt, width/2F-paint.measureText(txt, 0, txt.length())/2, width/2F, paint);
        }
    }

    private ContentValues extractData(Cursor cursor, String ColumnIndex){
        ContentValues valCV = new ContentValues();
        nVal=0;

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String value = cursor.getString(cursor.getColumnIndex(ColumnIndex));
            nVal++;
            if (valCV.get(value)==null){
                valCV.put(value, 1);
            }
            else{
                int i = valCV.getAsInteger(value)+1;
                valCV.put(value, i);
            }
            cursor.moveToNext();
        }
        return valCV;
    }

    public void setData(Cursor cursorPA, Cursor cursorG, Cursor cursorW, Cursor cursorP, Cursor cursorO, Cursor cursorT){
        ContentValues values = new ContentValues();
        nVal=0;
        extractData(cursorPA, values, context.getResources().getString(R.string.physicalActivity));
        extractData(cursorG, values, context.getResources().getString(R.string.glycemia));
        extractData(cursorW, values, context.getResources().getString(R.string.weight));
        extractData(cursorP, values, context.getResources().getString(R.string.pulse));
        extractData(cursorO, values, context.getResources().getString(R.string.oxygenSaturation));
        extractData(cursorT, values, context.getResources().getString(R.string.temperature));

        System.out.println("Value groups: " + values);

        visualizeData(values, "");
    }

    private void extractData(Cursor cursor, ContentValues valCV, String key){
        cursor.moveToFirst();
        valCV.put(key, 0);
        while (!cursor.isAfterLast()) {
            nVal++;
            int i = valCV.getAsInteger(key) + 1;
            valCV.put(key, i);
            cursor.moveToNext();
        }
    }
}
