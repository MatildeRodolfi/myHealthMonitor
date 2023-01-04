package com.example.myhealthmonitor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "myHealthMonitor.db";
    private static final int DB_VERSION = 1;

    // physical Activity Table name
    public static final String TAB_PHYSICAL_ACTIVITY = "physicalActivity";
    public static final String PA_ID = "_id";
    public static final String PA_DATE = "date";
    public static final String PA_TIME = "time";
    public static final String PA_ATTENTION = "attention";
    public static final String PA_STEPS = "steps";
    public static final String PA_CALORIES = "calories";
    public static final String PA_DISTANCE = "distance";
    public static final String PA_DURATION = "duration";
    public static final String PA_COMMENT = "comment";
    // physical Activity Table create statement
    private static final String TAB_PA_CREATE =
            "create table " + TAB_PHYSICAL_ACTIVITY + "( "
                    + PA_ID + " integer primary key autoincrement, "
                    + PA_DATE + " text not null, "
                    + PA_TIME + " text not null, "
                    + PA_ATTENTION + " integer, "
                    + PA_CALORIES + " integer not null, "
                    + PA_STEPS + " integer, "
                    + PA_DISTANCE + " real, "
                    + PA_DURATION + " integer, "
                    + PA_COMMENT + " text);";

    // glycemia Table name
    public static final String TAB_GLYCEMIA = "glycemia";
    public static final String G_ID = "_id";
    public static final String G_DATE = "date";
    public static final String G_TIME = "time";
    public static final String G_ATTENTION = "attention";
    public static final String G_VALUE = "value";
    public static final String G_LABEL = "label";
    public static final String G_HbA1C = "HbA1C";
    public static final String G_BREAD_UNIT = "breadUnit";
    public static final String G_BOLUS = "bolus";
    public static final String G_BASAL = "basal";
    public static final String G_COMMENT = "comment";
    // glycemia Table create statement
    private static final String TAB_G_CREATE =
            "create table " + TAB_GLYCEMIA + "( "
                    + G_ID + " integer primary key autoincrement, "
                    + G_DATE + " text not null, "
                    + G_TIME + " text not null, "
                    + G_ATTENTION + " integer, "
                    + G_VALUE + " real not null, "
                    + G_LABEL + " integer not null, "
                    + G_HbA1C + " real, "
                    + G_BREAD_UNIT + " integer, "
                    + G_BOLUS + " real, "
                    + G_BASAL + " real, "
                    + G_COMMENT + " text);";

    // weight Table name
    public static final String TAB_WEIGHT = "weight";
    public static final String W_ID = "_id";
    public static final String W_DATE = "date";
    public static final String W_TIME = "time";
    public static final String W_ATTENTION = "attention";
    public static final String W_VALUE = "value";
    public static final String W_MUSCLES = "muscles";
    public static final String W_BODY_FAT = "bodyFat";
    public static final String W_WATER = "water";
    public static final String W_COMMENT = "comment";
    // weight Table create statement
    private static final String TAB_W_CREATE =
            "create table " + TAB_WEIGHT + "( "
                    + W_ID + " integer primary key autoincrement, "
                    + W_DATE + " text not null, "
                    + W_TIME + " text not null, "
                    + W_ATTENTION + " integer, "
                    + W_VALUE + " integer not null, "
                    + W_MUSCLES + " integer, "
                    + W_BODY_FAT + " integer, "
                    + W_WATER + " integer, "
                    + W_COMMENT + " text);";

    // pulse Table name
    public static final String TAB_PULSE = "pulse";
    public static final String P_ID = "_id";
    public static final String P_DATE = "date";
    public static final String P_TIME = "time";
    public static final String P_ATTENTION = "attention";
    public static final String P_AVG = "avg";
    public static final String P_MAX = "max";
    public static final String P_MIN = "min";
    public static final String P_COMMENT = "comment";
    // pulse Table create statement
    private static final String TAB_P_CREATE =
            "create table " + TAB_PULSE + "( "
                    + P_ID + " integer primary key autoincrement, "
                    + P_DATE + " text not null, "
                    + P_TIME + " text not null, "
                    + P_ATTENTION + " integer, "
                    + P_AVG + " integer not null, "
                    + P_MAX + " integer, "
                    + P_MIN + " integer, "
                    + P_COMMENT + " text);";

    // oxygen Table name
    public static final String TAB_OXYGEN = "oxygen";
    public static final String O_ID = "_id";
    public static final String O_DATE = "date";
    public static final String O_TIME = "time";
    public static final String O_ATTENTION = "attention";
    public static final String O_AVG = "avg";
    public static final String O_MAX = "max";
    public static final String O_MIN = "min";
    public static final String O_COMMENT = "comment";
    // oxygen Table create statement
    private static final String TAB_O_CREATE =
            "create table " + TAB_OXYGEN + "( "
                    + O_ID + " integer primary key autoincrement, "
                    + O_DATE + " text not null, "
                    + O_TIME + " text not null, "
                    + O_ATTENTION + " integer, "
                    + O_AVG + " integer not null, "
                    + O_MAX + " integer, "
                    + O_MIN + " integer, "
                    + O_COMMENT + " text);";

    // temperature Table name
    public static final String TAB_TEMPERATURE = "temperature";
    public static final String T_ID = "_id";
    public static final String T_DATE = "date";
    public static final String T_TIME = "time";
    public static final String T_ATTENTION = "attention";
    public static final String T_VALUE = "temperatureVal";
    public static final String T_COMMENT = "comment";
    // temperature Table create statement
    private static final String TAB_T_CREATE =
            "create table " + TAB_TEMPERATURE + "( "
                    + T_ID + " integer primary key autoincrement, "
                    + T_DATE + " text not null, "
                    + T_TIME + " text not null, "
                    + T_ATTENTION + " integer, "
                    + T_VALUE + " real not null, "
                    + T_COMMENT + " text);";

    // control Table name
    public static final String TAB_ALARM = "alarm";
    public static final String A_ID = "_id";
    public static final String A_DATE = "date";
    public static final String A_REPEAT = "repeat";
    public static final String A_TIME = "time";
    public static final String A_NAME = "name";
    public static final String A_SOUND = "sound";
    public static final String A_VIBRATION = "vibration";
    // control Table create statement
    private static final String TAB_A_CREATE =
            "create table " + TAB_ALARM + "( "
                    + A_ID + " integer primary key autoincrement, "
                    + A_DATE + " text not null, "
                    + A_REPEAT + " integer not null, "
                    + A_TIME + " text not null, "
                    + A_NAME + " text, "
                    + A_SOUND + " integer not null, "
                    + A_VIBRATION + " integer not null);";

    // control Table name
    public static final String TAB_CONTROL = "control";
    public static final String C_ID = "_id";
    public static final String C_DATE = "date";
    public static final String C_REPEAT = "repeat";
    public static final String C_TIME = "time";
    public static final String C_TIMING = "timing";
    public static final String C_REPORT_TYPE = "reportType";
    public static final String C_VALUE_TYPE = "valueType";
    public static final String C_MAX = "max";
    public static final String C_MIN = "min";
    public static final String C_MAX_ATTENTION = "maxAttention";
    public static final String C_MIN_ATTENTION = "minAttention";
    public static final String C_SOUND = "sound";
    public static final String C_VIBRATION = "vibration";
    // control Table create statement
    private static final String TAB_C_CREATE =
            "create table " + TAB_CONTROL + "( "
                    + C_ID + " integer primary key autoincrement, "
                    + C_DATE + " text not null, "
                    + C_REPEAT + " integer not null, "
                    + C_TIME + " text not null, "
                    + C_TIMING + " integer not null, "
                    + C_REPORT_TYPE + " integer not null, "
                    + C_VALUE_TYPE + " integer not null, "
                    + C_MAX + " real, "
                    + C_MIN + " real, "
                    + C_MAX_ATTENTION + " integer, "
                    + C_MIN_ATTENTION + " integer, "
                    + C_SOUND + " integer not null, "
                    + C_VIBRATION + " integer not null);";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(TAB_PA_CREATE);
        database.execSQL(TAB_G_CREATE);
        database.execSQL(TAB_W_CREATE);
        database.execSQL(TAB_P_CREATE);
        database.execSQL(TAB_O_CREATE);
        database.execSQL(TAB_T_CREATE);
        database.execSQL(TAB_A_CREATE);
        database.execSQL(TAB_C_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        // Drop older table if existed
        database.execSQL("DROP TABLE IF EXISTS " + TAB_PHYSICAL_ACTIVITY);
        database.execSQL("DROP TABLE IF EXISTS " + TAB_GLYCEMIA);
        database.execSQL("DROP TABLE IF EXISTS " + TAB_WEIGHT);
        database.execSQL("DROP TABLE IF EXISTS " + TAB_PULSE);
        database.execSQL("DROP TABLE IF EXISTS " + TAB_OXYGEN);
        database.execSQL("DROP TABLE IF EXISTS " + TAB_TEMPERATURE);
        database.execSQL("DROP TABLE IF EXISTS " + TAB_ALARM);
        database.execSQL("DROP TABLE IF EXISTS " + TAB_CONTROL);
        // Create tables again
        onCreate(database);
    }

    public boolean insertNewPhysicalActivityReport(String date, String time, int attention, int steps, int calories, float distance, int duration, String comment) {
        ContentValues cv = new ContentValues();
        cv.put(PA_DATE, date);
        cv.put(PA_TIME, time);
        cv.put(PA_ATTENTION, attention);
        cv.put(PA_CALORIES, calories);
        if (steps > -23) {
            cv.put(PA_STEPS, steps);
        }
        if (distance!=-23){
            cv.put(PA_DISTANCE, distance);
        }
        if (duration!=-23){
            cv.put(PA_DURATION, duration);
        }
        if (comment!=null){
            cv.put(PA_COMMENT, comment);
        }

        long code = getWritableDatabase().insert(TAB_PHYSICAL_ACTIVITY, null, cv);
        return code > -1;
    }

    public Cursor getPhysicalActivityReportsOfDay(String day) {
        return getWritableDatabase().query(TAB_PHYSICAL_ACTIVITY, null, PA_DATE + "=?", new String[]{day}, null, null, PA_TIME);
    }

    public Cursor getPhysicalActivityReport(String id) {
        return getWritableDatabase().query(TAB_PHYSICAL_ACTIVITY, null, PA_ID + "=?", new String[]{id}, null, null, null);
    }

    public Cursor getPhysicalActivityReportsOverPeriod(String firstDay, String lastDay) {
        if (firstDay.equals(lastDay)){
            return getWritableDatabase().query(TAB_PHYSICAL_ACTIVITY, null, PA_DATE + " BETWEEN? AND?", new String[]{firstDay, lastDay}, null, null, PA_TIME);
        }
        else{
            return getWritableDatabase().query(TAB_PHYSICAL_ACTIVITY, null, PA_DATE + " BETWEEN? AND?", new String[]{firstDay, lastDay}, null, null, PA_DATE);
        }

    }

    public boolean modifyPhysicalActivityReport(String id, String date, String time, int attention, int steps, int calories, float distance, int duration, String comment) {
        ContentValues cv = new ContentValues();
        cv.put(PA_DATE, date);
        cv.put(PA_TIME, time);
        cv.put(PA_ATTENTION, attention);
        cv.put(PA_CALORIES, calories);
        if (steps > -23) {
            cv.put(PA_STEPS, steps);
        }
        else{
            cv.put(PA_STEPS, (byte[]) null);
        }
        if (distance!=-23){
            cv.put(PA_DISTANCE, distance);
        }
        else{
            cv.put(PA_DISTANCE, (byte[]) null);
        }
        if (duration!=-23){
            cv.put(PA_DURATION, duration);
        }
        else{
            cv.put(PA_DURATION, (byte[]) null);
        }
        if (comment!=null){
            cv.put(PA_COMMENT, comment);
        }
        else{
            cv.put(PA_COMMENT, (byte[]) null);
        }

        long code = getWritableDatabase().update(TAB_PHYSICAL_ACTIVITY, cv, PA_ID + "=?", new String[]{id});
        return code > 0;
    }

    public boolean deletePhysicalActivityReport(String id) {
        long code = getWritableDatabase().delete(TAB_PHYSICAL_ACTIVITY, PA_ID + "=?", new String[]{id});
        return code > 0;
    }

    public boolean insertNewGlycemiaReport(String date, String time, int attention, float value, int label, float HbA1C, int breadUnit, float bolus, float basal, String comment) {
        ContentValues cv = new ContentValues();
        cv.put(G_DATE, date);
        cv.put(G_TIME, time);
        cv.put(G_ATTENTION, attention);
        cv.put(G_VALUE, value);
        cv.put(G_LABEL, label);
        if (HbA1C!=-23){
            cv.put(G_HbA1C, HbA1C);
        }
        if (breadUnit!=-23){
            cv.put(G_BREAD_UNIT, breadUnit);
        }
        if (bolus!=-23){
            cv.put(G_BOLUS, bolus);
        }
        if (basal!=-23){
            cv.put(G_BASAL, basal);
        }
        if (comment!=null){
            cv.put(G_COMMENT, comment);
        }

        long code = getWritableDatabase().insert(TAB_GLYCEMIA, null, cv);
        return code > -1;
    }

    public Cursor getGlycemiaReportsOfDay(String day) {
        return getWritableDatabase().query(TAB_GLYCEMIA, null, G_DATE + "=?", new String[]{day}, null, null, G_TIME);
    }

    public Cursor getGlycemiaReport(String id) {
        return getWritableDatabase().query(TAB_GLYCEMIA, null, G_ID + "=?", new String[]{id}, null, null, null);
    }

    public Cursor getGlycemiaReportsOverPeriod(String firstDay, String lastDay) {
        if (firstDay.equals(lastDay)){
            return getWritableDatabase().query(TAB_GLYCEMIA, null, G_DATE + " BETWEEN? AND?", new String[]{firstDay, lastDay}, null, null, G_TIME);
        }
        else{
            return getWritableDatabase().query(TAB_GLYCEMIA, null, G_DATE + " BETWEEN? AND?", new String[]{firstDay, lastDay}, null, null, G_DATE);
        }
    }

    public boolean modifyGlycemiaReport(String id, String date, String time, int attention, float value, int label, float HbA1C, int breadUnit, float bolus, float basal, String comment) {
        ContentValues cv = new ContentValues();
        cv.put(G_DATE, date);
        cv.put(G_TIME, time);
        cv.put(G_ATTENTION, attention);
        cv.put(G_VALUE, value);
        cv.put(G_LABEL, label);
        if (HbA1C!=-23){
            cv.put(G_HbA1C, HbA1C);
        } else{
            cv.put(G_HbA1C, (byte[]) null);
        }
        if (breadUnit!=-23){
            cv.put(G_BREAD_UNIT, breadUnit);
        } else{
            cv.put(G_BREAD_UNIT, (byte[]) null);
        }
        if (bolus!=-23){
            cv.put(G_BOLUS, bolus);
        } else{
            cv.put(G_BOLUS, (byte[]) null);
        }
        if (basal!=-23){
            cv.put(G_BASAL, basal);
        } else{
            cv.put(G_BASAL, (byte[]) null);
        }
        if (comment!=null){
            cv.put(G_COMMENT, comment);
        } else{
            cv.put(G_COMMENT, (byte[]) null);
        }

        long code = getWritableDatabase().update(TAB_GLYCEMIA, cv, G_ID + "=?", new String[]{id});
        return code > 0;
    }

    public boolean deleteGlycemiaReport(String id) {
        long code = getWritableDatabase().delete(TAB_GLYCEMIA, G_ID + "=?", new String[]{id});
        return code > 0;
    }

    public boolean insertNewWeightReport(String date, String time, int attention, float value, float muscles, float bodyFat, float water, String comment) {
        ContentValues cv = new ContentValues();
        cv.put(W_DATE, date);
        cv.put(W_TIME, time);
        cv.put(W_ATTENTION, attention);
        cv.put(W_VALUE, value);
        if (muscles!=-23){
            cv.put(W_MUSCLES, muscles);
        }
        if (bodyFat!=-23){
            cv.put(W_BODY_FAT, bodyFat);
        }
        if (water!=-23){
            cv.put(W_WATER, water);
        }
        if (comment!=null){
            cv.put(G_COMMENT, comment);
        }

        long code = getWritableDatabase().insert(TAB_WEIGHT, null, cv);
        return code > -1;
    }

    public Cursor getWeightReportsOfDay(String day) {
        return getWritableDatabase().query(TAB_WEIGHT, null, W_DATE + "=?", new String[]{day}, null, null, W_TIME);
    }

    public Cursor getWeightReport(String id) {
        return getWritableDatabase().query(TAB_WEIGHT, null, W_ID + "=?", new String[]{id}, null, null, null);
    }

    public Cursor getWeightReportsOverPeriod(String firstDay, String lastDay) {
        if (firstDay.equals(lastDay)){
            return getWritableDatabase().query(TAB_WEIGHT, null, W_DATE + " BETWEEN? AND?", new String[]{firstDay, lastDay}, null, null, W_TIME);
        }
        else{
            return getWritableDatabase().query(TAB_WEIGHT, null, W_DATE + " BETWEEN? AND?", new String[]{firstDay, lastDay}, null, null, W_DATE);
        }
    }

    public boolean modifyWeightReport(String id, String date, String time, int attention, float value, float muscles, float bodyFat, float water, String comment) {
        ContentValues cv = new ContentValues();
        cv.put(W_DATE, date);
        cv.put(W_TIME, time);
        cv.put(W_ATTENTION, attention);
        cv.put(W_VALUE, value);
        if (muscles!=-23){
            cv.put(W_MUSCLES, muscles);
        } else{
            cv.put(W_MUSCLES, (byte[]) null);
        }
        if (bodyFat!=-23){
            cv.put(W_BODY_FAT, bodyFat);
        } else{
            cv.put(W_BODY_FAT, (byte[]) null);
        }
        if (water!=-23){
            cv.put(W_WATER, water);
        } else{
            cv.put(W_WATER, (byte[]) null);
        }
        if (comment!=null){
            cv.put(W_COMMENT, comment);
        } else{
            cv.put(W_COMMENT, (byte[]) null);
        }

        long code = getWritableDatabase().update(TAB_WEIGHT, cv, W_ID + "=?", new String[]{id});
        return code > 0;
    }

    public boolean deleteWeightReport(String id) {
        long code = getWritableDatabase().delete(TAB_WEIGHT, W_ID + "=?", new String[]{id});
        return code > 0;
    }

    public boolean insertNewPulseReport(String date, String time, int attention, int avg, int max, int min, String comment) {
        ContentValues cv = new ContentValues();
        cv.put(P_DATE, date);
        cv.put(P_TIME, time);
        cv.put(P_ATTENTION, attention);
        cv.put(P_AVG, avg);
        if (max!=-23){
            cv.put(P_MAX, max);
        }
        if (min!=-23){
            cv.put(P_MIN, min);
        }
        if (comment!=null){
            cv.put(P_COMMENT, comment);
        }

        long code = getWritableDatabase().insert(TAB_PULSE, null, cv);
        return code > -1;
    }

    public Cursor getPulseReportsOfDay(String day) {
        return getWritableDatabase().query(TAB_PULSE, null, P_DATE + "=?", new String[]{day}, null, null, P_TIME);
    }

    public Cursor getPulseReport(String id) {
        return getWritableDatabase().query(TAB_PULSE, null, P_ID + "=?", new String[]{id}, null, null, null);
    }

    public Cursor getPulseReportsOverPeriod(String firstDay, String lastDay) {
        if (firstDay.equals(lastDay)){
            return getWritableDatabase().query(TAB_PULSE, null, P_DATE + " BETWEEN? AND?", new String[]{firstDay, lastDay}, null, null, P_TIME);
        }
        else{
            return getWritableDatabase().query(TAB_PULSE, null, P_DATE + " BETWEEN? AND?", new String[]{firstDay, lastDay}, null, null, P_DATE);
        }
    }

    public boolean modifyPulseReport(String id, String date, String time, int attention, int avg, int max, int min, String comment) {
        ContentValues cv = new ContentValues();
        cv.put(P_DATE, date);
        cv.put(P_TIME, time);
        cv.put(P_ATTENTION, attention);
        cv.put(P_AVG, avg);
        if (max!=-23){
            cv.put(P_MAX, max);
        }  else{
            cv.put(P_MAX, (byte[]) null);
        }
        if (min!=-23){
            cv.put(P_MIN, min);
        } else{
            cv.put(P_MIN, (byte[]) null);
        }
        if (comment!=null){
            cv.put(P_COMMENT, comment);
        } else{
            cv.put(P_COMMENT, (byte[]) null);
        }

        System.out.println("ID: " + id);
        long code = getWritableDatabase().update(TAB_PULSE, cv, P_ID + "=?", new String[]{id});
        System.out.println("Rows: " + code);
        return code > 0;
    }

    public boolean deletePulseReport(String id) {
        long code = getWritableDatabase().delete(TAB_PULSE, P_ID + "=?", new String[]{id});
        return code > 0;
    }

    public boolean insertNewOxygenReport(String date, String time, int attention, int avg, int max, int min, String comment) {
        ContentValues cv = new ContentValues();
        cv.put(O_DATE, date);
        cv.put(O_TIME, time);
        cv.put(O_ATTENTION, attention);
        cv.put(O_AVG, avg);
        if (max!=-23){
            cv.put(O_MAX, max);
        }
        if (min!=-23){
            cv.put(O_MIN, min);
        }
        if (comment!=null){
            cv.put(O_COMMENT, comment);
        }

        long code = getWritableDatabase().insert(TAB_OXYGEN, null, cv);
        return code > -1;
    }

    public Cursor getOxygenReportsOfDay(String day) {
        return getWritableDatabase().query(TAB_OXYGEN, null, O_DATE + "=?", new String[]{day}, null, null, O_TIME);
    }

    public Cursor getOxygenReport(String id) {
        return getWritableDatabase().query(TAB_OXYGEN, null, O_ID + "=?", new String[]{id}, null, null, null);
    }

    public Cursor getOxygenReportsOverPeriod(String firstDay, String lastDay) {
        if (firstDay.equals(lastDay)){
            return getWritableDatabase().query(TAB_OXYGEN, null, O_DATE + " BETWEEN? AND?", new String[]{firstDay, lastDay}, null, null, O_TIME);
        }
        else{
            return getWritableDatabase().query(TAB_OXYGEN, null, O_DATE + " BETWEEN? AND?", new String[]{firstDay, lastDay}, null, null, O_DATE);
        }
    }

    public boolean modifyOxygenReport(String id, String date, String time, int attention, int avg, int max, int min, String comment) {
        ContentValues cv = new ContentValues();
        cv.put(O_DATE, date);
        cv.put(O_TIME, time);
        cv.put(O_ATTENTION, attention);
        cv.put(O_AVG, avg);
        if (max!=-23){
            cv.put(O_MAX, max);
        } else{
            cv.put(O_MAX, (byte[]) null);
        }
        if (min!=-23){
            cv.put(O_MIN, min);
        } else{
            cv.put(O_MIN, (byte[]) null);
        }
        if (comment!=null){
            cv.put(O_COMMENT, comment);
        } else{
            cv.put(O_COMMENT, (byte[]) null);
        }

        long code = getWritableDatabase().update(TAB_OXYGEN, cv, O_ID + "=?", new String[]{id});
        return code > 0;
    }

    public boolean deleteOxygenReport(String id) {
        long code = getWritableDatabase().delete(TAB_OXYGEN, O_ID + "=?", new String[]{id});
        return code > 0;
    }

    public boolean insertNewTemperatureReport(String date, String time, int attention, float value, String comment) {
        ContentValues cv = new ContentValues();
        cv.put(T_DATE, date);
        cv.put(T_TIME, time);
        cv.put(T_ATTENTION, attention);
        cv.put(T_VALUE, value);
        if (comment!=null){
            cv.put(T_COMMENT, comment);
        }

        long code = getWritableDatabase().insert(TAB_TEMPERATURE, null, cv);
        return code > -1;
    }

    public Cursor getTemperatureReportsOfDay(String day) {
        return getWritableDatabase().query(TAB_TEMPERATURE, null, T_DATE + "=?", new String[]{day}, null, null, T_TIME);
    }

    public Cursor getTemperatureReport(String id) {
        return getWritableDatabase().query(TAB_TEMPERATURE, null, T_ID + "=?", new String[]{id}, null, null, null);
    }

    public Cursor getTemperatureReportsOverPeriod(String firstDay, String lastDay) {
        if (firstDay.equals(lastDay)){
            return getWritableDatabase().query(TAB_TEMPERATURE, null, T_DATE + " BETWEEN? AND?", new String[]{firstDay, lastDay}, null, null, T_TIME);
        }
        else{
            return getWritableDatabase().query(TAB_TEMPERATURE, null, T_DATE + " BETWEEN? AND?", new String[]{firstDay, lastDay}, null, null, T_DATE);
        }
    }

    public boolean modifyTemperatureReport(String id, String date, String time, int attention, float value, String comment) {
        ContentValues cv = new ContentValues();
        cv.put(T_DATE, date);
        cv.put(T_TIME, time);
        cv.put(T_ATTENTION, attention);
        cv.put(T_VALUE, value);
        if (comment!=null){
            cv.put(T_COMMENT, comment);
        } else{
            cv.put(T_COMMENT, (byte[]) null);
        }

        long code = getWritableDatabase().update(TAB_TEMPERATURE, cv, T_ID + "=?", new String[]{id});
        return code > 0;
    }

    public boolean deleteTemperatureReport(String id) {
        long code = getWritableDatabase().delete(TAB_TEMPERATURE, T_ID + "=?", new String[]{id});
        return code > 0;
    }

    public long insertNewAlarm(String date, boolean repeat, String time, String name, boolean sound, boolean vibration) {
        ContentValues cv = new ContentValues();
        cv.put(A_DATE, date);
        if (repeat){
            cv.put(A_REPEAT, 1);
        } else{
            cv.put(A_REPEAT, 0);
        }
        cv.put(A_TIME, time);
        if (name!=null){
            cv.put(A_NAME, name);
        }
        if (sound){
            cv.put(A_SOUND, 1);
        } else{
            cv.put(A_SOUND, 0);
        }
        if (vibration){
            cv.put(A_VIBRATION, 1);
        } else{
            cv.put(A_VIBRATION, 0);
        }

        return getWritableDatabase().insert(TAB_ALARM, null, cv);
    }

    public Cursor getAlarms() {
        return getWritableDatabase().query(TAB_ALARM, null, null, null, null, null, A_ID);
    }

    public Cursor getAlarm(String id) {
        return getWritableDatabase().query(TAB_ALARM, null, A_ID + "=?", new String[]{id}, null, null, null);
    }

    public boolean modifyAlarm(String id, String date, boolean repeat, String time, String name, boolean sound, boolean vibration) {
        ContentValues cv = new ContentValues();
        cv.put(A_DATE, date);
        if (repeat){
            cv.put(A_REPEAT, 1);
        } else{
            cv.put(A_REPEAT, 0);
        }
        cv.put(A_TIME, time);
        if (name!=null){
            cv.put(A_NAME, name);
        } else{
            cv.put(A_NAME, (byte[]) null);
        }
        if (sound){
            cv.put(A_SOUND, 1);
        } else{
            cv.put(A_SOUND, 0);
        }
        if (vibration){
            cv.put(A_VIBRATION, 1);
        } else{
            cv.put(A_VIBRATION, 0);
        }

        long code = getWritableDatabase().update(TAB_ALARM, cv, A_ID + "=?", new String[]{id});
        return code > 0;
    }

    public boolean deleteAlarm(String id) {
        long code = getWritableDatabase().delete(TAB_ALARM, A_ID + "=?", new String[]{id});
        return code > 0;
    }

    public long insertNewControl(String date, boolean repeat, String time, int timing, int reportType, int valueType, float max, float min, int maxAttention, int minAttention, boolean sound, boolean vibration) {
        ContentValues cv = new ContentValues();
        cv.put(C_DATE, date);
        if (repeat){
            cv.put(C_REPEAT, 1);
        } else{
            cv.put(C_REPEAT, 0);
        }
        cv.put(C_TIME, time);
        cv.put(C_TIMING, timing);
        cv.put(C_REPORT_TYPE, reportType);
        cv.put(C_VALUE_TYPE, valueType);
        if (max!=-23){
            cv.put(C_MAX, max);
        } else{
            cv.put(C_MAX, (byte[]) null);
        }
        if (min!=-23){
            cv.put(C_MIN, min);
        } else{
            cv.put(C_MIN, (byte[]) null);
        }
        cv.put(C_MIN_ATTENTION, minAttention);
        cv.put(C_MAX_ATTENTION, maxAttention);
        if (sound){
            cv.put(C_SOUND, 1);
        } else{
            cv.put(C_SOUND, 0);
        }
        if (vibration){
            cv.put(C_VIBRATION, 1);
        } else{
            cv.put(C_VIBRATION, 0);
        }

        return getWritableDatabase().insert(TAB_CONTROL, null, cv);
    }

    public Cursor getControls() {
        return getWritableDatabase().query(TAB_CONTROL, null, null, null, null, null, C_ID);
    }

    public Cursor getControl(String id) {
        return getWritableDatabase().query(TAB_CONTROL, null, C_ID + "=?", new String[]{id}, null, null, null);
    }

    public boolean modifyControl(String id, String date, boolean repeat, String time, int timing, int reportType, int valueType, float max, float min, int maxAttention, int minAttention, boolean sound, boolean vibration) {
        ContentValues cv = new ContentValues();
        cv.put(C_DATE, date);
        if (repeat){
            cv.put(C_REPEAT, 1);
        } else{
            cv.put(C_REPEAT, 0);
        }
        cv.put(C_TIME, time);
        cv.put(C_TIMING, timing);
        cv.put(C_REPORT_TYPE, reportType);
        cv.put(C_VALUE_TYPE, valueType);
        if (max!=-23){
            cv.put(C_MAX, max);
        }
        if (min!=-23){
            cv.put(C_MIN, min);
        }
        cv.put(C_MIN_ATTENTION, minAttention);
        cv.put(C_MAX_ATTENTION, maxAttention);
        if (sound){
            cv.put(C_SOUND, 1);
        } else{
            cv.put(C_SOUND, 0);
        }
        if (vibration){
            cv.put(C_VIBRATION, 1);
        } else{
            cv.put(C_VIBRATION, 0);
        }

        long code = getWritableDatabase().update(TAB_CONTROL, cv, C_ID + "=?", new String[]{id});
        return code > 0;
    }

    public boolean deleteControl(String id) {
        long code = getWritableDatabase().delete(TAB_CONTROL, C_ID + "=?", new String[]{id});
        return code > 0;
    }
}
