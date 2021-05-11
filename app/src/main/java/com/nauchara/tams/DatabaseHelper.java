package com.nauchara.tams;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_OPNAME_HEADER = "OPNAME_HEADER";
    public static final String TABLE_OPNAME_DETAIL = "OPNAME_DETAIL";
    public static final String TABLE_LOCATION = "LOCATION";

    public static final String COLUMN_OH_ID = "OH_ID";
    public static final String COLUMN_OH_OP_ID = "OP_ID";
    public static final String COLUMN_OH_OP_TITLE = "OP_TITLE";
    public static final String COLUMN_OH_OP_DATE = "OP_DATE";
    public static final String COLUMN_OH_OP_STATUS_1 = "OP_STATUS1";
    public static final String COLUMN_OH_OP_STATUS_2 = "OP_STATUS2";
    public static final String COLUMN_OH_OP_STATUS_3 = "OP_STATUS3";
    public static final String COLUMN_OH_OP_NAME = "OP_NAME";
    public static final String COLUMN_OH_OP_STATUS_4 = "OP_STATUS4";

    public static final String COLUMN_OD_ID = "OD_ID";
    public static final String COLUMN_OD_OD_VALUE_1 = "OD_VALUE1";
    public static final String COLUMN_OD_OD_VALUE_2 = "OD_VALUE2";
    public static final String COLUMN_OD_OD_VALUE_3 = "OD_VALUE3";
    public static final String COLUMN_OD_OD_ITEMNAME = "OD_ITEMNAME";
    public static final String COLUMN_OD_OD_VALUE_4 = "OD_VALUE4";
    public static final String COLUMN_OD_OD_TAG = "OD_TAG";
    public static final String COLUMN_OD_OD_LOCATION = "OD_LOCATION";
    public static final String COLUMN_OD_OD_LOCATION_DESC = "OD_LOCATION_DESC";

    public static final String COLUMN_LOC_ID = "ID";
    public static final String COLUMN_LOC_VALUE1 = "VALUE1";
    public static final String COLUMN_LOC_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_LOC_VALUE2 = "VALUE2";
    public static final String COLUMN_LOC_ROOM = "ROOM";
    public static final String COLUMN_LOC_BUILDING = "BUILDING";
    public static final String COLUMN_LOC_FLOOR = "FLOOR";

    public static final String TABLE_SETTINGS = "SETTINGS";
    public static final String COLUMN_SET_NAMES = "NAMES";
    public static final String COLUMN_SET_VALUE1 = "VALUE1";
    public static final String COLUMN_SET_VALUE2 = "VALUE2";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "tams.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableTOO = "CREATE TABLE " + TABLE_OPNAME_HEADER + " (" + COLUMN_OH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_OH_OP_ID + " INT, " + COLUMN_OH_OP_TITLE + " TEXT, " + COLUMN_OH_OP_DATE + " TEXT, " + COLUMN_OH_OP_STATUS_1 + " INT, " + COLUMN_OH_OP_STATUS_2 + " INT, " +
                COLUMN_OH_OP_STATUS_3 + " TEXT, " + COLUMN_OH_OP_NAME + " TEXT, " + COLUMN_OH_OP_STATUS_4 + " TEXT)";
        db.execSQL(createTableTOO);

        String createTableTOOD = "CREATE TABLE " + TABLE_OPNAME_DETAIL + " (" + COLUMN_OD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_OD_OD_VALUE_1 + " INT," +
                COLUMN_OD_OD_VALUE_2 + " INT, " + COLUMN_OD_OD_VALUE_3 + " INT, " + COLUMN_OD_OD_ITEMNAME + " TEXT, " + COLUMN_OD_OD_VALUE_4 + " INT, "+ COLUMN_OD_OD_TAG + " TEXT, " +
                COLUMN_OD_OD_LOCATION + " INT, " + COLUMN_OD_OD_LOCATION_DESC + " TEXT)";
        db.execSQL(createTableTOOD);

        String CreateTableLocation = "CREATE TABLE " + TABLE_LOCATION + " (" + COLUMN_LOC_ID + " INTEGER PRIMARY KEY, " + COLUMN_LOC_VALUE1 + " INT, " + COLUMN_LOC_DESCRIPTION + " TEXT," +
                COLUMN_LOC_VALUE2 + " INT, " + COLUMN_LOC_ROOM + " TEXT, " + COLUMN_LOC_BUILDING + " TEXT, " + COLUMN_LOC_FLOOR + " TEXT)";
        db.execSQL(CreateTableLocation);

        String CreateTableSettings = "CREATE TABLE " + TABLE_SETTINGS + " (" + COLUMN_SET_NAMES + " TEXT, " + COLUMN_SET_VALUE1 + " INT, "+COLUMN_SET_VALUE2+" TEXT)";
        db.execSQL(CreateTableSettings);

        String InsertTableSettings = "INSERT INTO " + TABLE_SETTINGS + " ("+COLUMN_SET_NAMES+", "+COLUMN_SET_VALUE1+", "+COLUMN_SET_VALUE2+") VALUES ('ANTENA', '50', '-')";
        db.execSQL(InsertTableSettings);
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addHeader(OpnameHModel opnameHModel) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_OH_OP_ID, opnameHModel.getOph_id());
        cv.put(COLUMN_OH_OP_TITLE, opnameHModel.getOph_title());
        cv.put(COLUMN_OH_OP_DATE, opnameHModel.getOph_date());
        cv.put(COLUMN_OH_OP_STATUS_1, opnameHModel.getOph_status1());
        cv.put(COLUMN_OH_OP_STATUS_2, opnameHModel.getOph_status2());
        cv.put(COLUMN_OH_OP_STATUS_3, opnameHModel.getOph_status3());
        cv.put(COLUMN_OH_OP_NAME, opnameHModel.getOph_name());

        long insert = db.insert(TABLE_OPNAME_HEADER, null, cv);

        if(insert == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean addLocation(LocationModel locationModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_LOC_ID, locationModel.getLoc_id());
        cv.put(COLUMN_LOC_VALUE1, locationModel.getLoc_value1());
        cv.put(COLUMN_LOC_DESCRIPTION, locationModel.getLoc_description());
        cv.put(COLUMN_LOC_VALUE2, locationModel.getLoc_value2());
        cv.put(COLUMN_LOC_ROOM, locationModel.getLoc_room());
        cv.put(COLUMN_LOC_BUILDING, locationModel.getLoc_building());
        cv.put(COLUMN_LOC_FLOOR, locationModel.getLoc_floor());

        long insert = db.insert(TABLE_LOCATION, null, cv);

        if(insert == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean addDetail(OpnameDModel opnameDModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_OD_ID, opnameDModel.getOpd_id());
        cv.put(COLUMN_OD_OD_VALUE_1, opnameDModel.getOpd_value1());
        cv.put(COLUMN_OD_OD_VALUE_2, opnameDModel.getOpd_value2());
        cv.put(COLUMN_OD_OD_VALUE_3, opnameDModel.getOpd_value3());
        cv.put(COLUMN_OD_OD_ITEMNAME, opnameDModel.getOpd_itemname());
        cv.put(COLUMN_OD_OD_VALUE_4, opnameDModel.getOpd_value4());
        cv.put(COLUMN_OD_OD_TAG, opnameDModel.getOpd_tag());
        cv.put(COLUMN_OD_OD_LOCATION, opnameDModel.getOpd_location());
        cv.put(COLUMN_OD_OD_LOCATION_DESC, opnameDModel.getOpd_location_desc());

        long insert = db.insert(TABLE_OPNAME_DETAIL, null, cv);

        if(insert == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean closeHeader(int intIDHeader) {
        String queryString = "UPDATE "+TABLE_OPNAME_HEADER+" SET "+COLUMN_OH_OP_STATUS_1+" = '2' WHERE "+
                COLUMN_OH_OP_ID +" = '"+intIDHeader+"'";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean delHeader() {
        String queryString = "DELETE FROM "+TABLE_OPNAME_HEADER;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean delDetailAll() {
        String queryString = "DELETE FROM "+TABLE_OPNAME_DETAIL;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean delLocationAll() {
        String queryString = "DELETE FROM "+TABLE_LOCATION;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean checkHeader(int intHeaderID) {
        String queryString = "SELECT "+COLUMN_OH_OP_ID+" FROM "+TABLE_OPNAME_HEADER+" WHERE "+COLUMN_OH_OP_ID+" = '"+intHeaderID+"'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return true;
        }
        else {
            cursor.close();
            db.close();
            return false;
        }
    }

    public boolean checkDetail(int intDetailID) {
        String queryString = "SELECT "+COLUMN_OD_ID+" FROM "+TABLE_OPNAME_DETAIL+" WHERE "+COLUMN_OD_ID+" = '"+intDetailID+"'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return true;
        }
        else {
            cursor.close();
            db.close();
            return false;
        }
    }

    public boolean checkLocation(int intLocationID) {
        String queryString = "SELECT "+COLUMN_LOC_ID+" FROM "+TABLE_LOCATION+" WHERE "+COLUMN_LOC_ID+" = "+intLocationID;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return true;
        }
        else {
            cursor.close();
            db.close();
            return false;
        }
    }

    public int getAntenaSignal() {
        int antenasignal = 0;
        String queryString = "SELECT "+COLUMN_SET_VALUE1+" FROM "+TABLE_SETTINGS+" WHERE NAMES = 'ANTENA'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            do {
                antenasignal = cursor.getInt(0);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return antenasignal;
    }

    public int getCountDetail(int idHeader) {
        int countdetail = 0;
        String queryString = "SELECT COUNT(1) FROM " +TABLE_OPNAME_DETAIL+ " WHERE "+COLUMN_OD_OD_VALUE_1+" = "+idHeader;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            countdetail = cursor.getInt(0);
        }
        while(cursor.moveToNext());

        cursor.close();
        db.close();

        return countdetail;
    }

    public int getCountDetailExist(int idHeader) {
        int countdetail = 0;
        String queryString = "SELECT COUNT(1) FROM " +TABLE_OPNAME_DETAIL+ " WHERE "+COLUMN_OD_OD_VALUE_1+" = "+idHeader+" AND "+
                COLUMN_OD_OD_VALUE_3 +" = 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            countdetail = cursor.getInt(0);
        }
        while(cursor.moveToNext());

        cursor.close();
        db.close();

        return countdetail;
    }

    public boolean updateOPDetail(int intHeaderID, String strTag, int intLocation, String strLocation) {
        String queryString = "UPDATE "+TABLE_OPNAME_DETAIL+ " SET "+COLUMN_OD_OD_VALUE_3+" = '1', "
                + COLUMN_OD_OD_LOCATION +" = '"+intLocation+"', "+COLUMN_OD_OD_LOCATION_DESC+" = '"+strLocation+"' WHERE " +
                COLUMN_OD_OD_VALUE_1 + " = '"+ intHeaderID +"' AND " +
                COLUMN_OD_OD_TAG + " = '"+strTag+"' AND "+COLUMN_OD_OD_VALUE_3+" = '-1'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return true;
        }
        else {
            cursor.close();
            db.close();
            return false;
        }
    }

    public boolean deleteOPDetail(int intHeaderID, String strTag, int intLocation, String strLocation) {
        String queryString = "UPDATE "+TABLE_OPNAME_DETAIL+ " SET "+COLUMN_OD_OD_VALUE_3+" = '-1', "
                + COLUMN_OD_OD_LOCATION +" = '"+intLocation+"', "+COLUMN_OD_OD_LOCATION_DESC+" = '"+strLocation+"' WHERE " +
                COLUMN_OD_OD_VALUE_1 + " = '"+ intHeaderID +"' AND " +
                COLUMN_OD_OD_TAG + " = '"+strTag+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return true;
        }
        else {
            cursor.close();
            db.close();
            return false;
        }
    }

    public void setAntenaSignal(int signalvalue) {
        String queryString = "UPDATE "+TABLE_SETTINGS+" SET "+COLUMN_SET_VALUE1+" = "+signalvalue+" WHERE NAMES = 'ANTENA'";
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(queryString);
        db.close();
    }

    public int getStatusHeader(int intHeaderID) {
        String queryString = "SELECT "+COLUMN_OH_OP_STATUS_1+" FROM "+TABLE_OPNAME_HEADER+" WHERE "+COLUMN_OH_OP_ID+" = '"+intHeaderID+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        int statusHeader = 0;
        if(cursor.moveToFirst()) {
            do {
                statusHeader = cursor.getInt(0);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return statusHeader;
    }

    public List<LocationModel> getLocation() {
        List<LocationModel> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM "+ TABLE_LOCATION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            do {
                int loc_id = cursor.getInt(0);
                int loc_value1 = cursor.getInt(1);
                String loc_description = cursor.getString(2);
                int loc_value2 = cursor.getInt(3);
                String loc_room = cursor.getString(4);
                String loc_building = cursor.getString(5);
                String loc_floor = cursor.getString(6);

                LocationModel locationModel = new LocationModel(loc_id, loc_value1, loc_description,
                        loc_value2, loc_room, loc_building, loc_floor);
                returnList.add(locationModel);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return returnList;
    }

    public List<OpnameDModel> getOPDetail(int oph_id, int oph_filter) {
        List<OpnameDModel> returnList = new ArrayList<>();

        String queryString = "";
        if(oph_filter == 0) {
            queryString = "SELECT * FROM " + TABLE_OPNAME_DETAIL + " WHERE " + COLUMN_OD_OD_VALUE_1 + " = " + oph_id;
        }
        else {
            queryString = "SELECT * FROM " + TABLE_OPNAME_DETAIL + " WHERE " + COLUMN_OD_OD_VALUE_1 + " = " + oph_id + " AND "+COLUMN_OD_OD_VALUE_3+" = "+oph_filter;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            do {
                int opd_id = cursor.getInt(0);
                int opd_value1 = cursor.getInt(1);
                int opd_value2 = cursor.getInt(2);
                int opd_value3 = cursor.getInt(3);
                String opd_itemname = cursor.getString(4);
                int opd_value4 = cursor.getInt(5);
                String opd_tag = cursor.getString(6);
                int opd_location = cursor.getInt(7);
                String opd_location_desc = cursor.getString(8);

                OpnameDModel opnameDModel = new OpnameDModel(opd_id, opd_value1, opd_value2, opd_value3, opd_itemname,
                        opd_value4, opd_tag, opd_location, opd_location_desc);
                returnList.add(opnameDModel);
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return returnList;
    }

    public List<OpnameDModel> getOPDetailSearch(int oph_id, int oph_filter, String oph_search) {
        List<OpnameDModel> returnList = new ArrayList<>();

        String queryString = "";
        if(oph_filter == 0) {
            queryString = "SELECT * FROM " + TABLE_OPNAME_DETAIL + " WHERE " + COLUMN_OD_OD_VALUE_1 + " = " + oph_id;
        }
        else {
            queryString = "SELECT * FROM " + TABLE_OPNAME_DETAIL + " WHERE " + COLUMN_OD_OD_VALUE_1 + " = " + oph_id + " AND "+COLUMN_OD_OD_VALUE_3+" = "+oph_filter+" AND "+COLUMN_OD_OD_ITEMNAME+" LIKE '%"+oph_search+"%'";
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            do {
                int opd_id = cursor.getInt(0);
                int opd_value1 = cursor.getInt(1);
                int opd_value2 = cursor.getInt(2);
                int opd_value3 = cursor.getInt(3);
                String opd_itemname = cursor.getString(4);
                int opd_value4 = cursor.getInt(5);
                String opd_tag = cursor.getString(6);
                int opd_location = cursor.getInt(7);
                String opd_location_desc = cursor.getString(8);

                OpnameDModel opnameDModel = new OpnameDModel(opd_id, opd_value1, opd_value2, opd_value3, opd_itemname,
                        opd_value4, opd_tag, opd_location, opd_location_desc);
                returnList.add(opnameDModel);
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return returnList;
    }

    public List<OpnameHModel> getOPHeader() {
        List<OpnameHModel> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM "+ TABLE_OPNAME_HEADER + " ORDER BY "+COLUMN_OH_OP_ID+" DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {

            do {

                int oph_id = cursor.getInt(1);

                int intAssetExist = getCountDetailExist(oph_id);
                int intAssetCount = getCountDetail(oph_id);

                String oph_title = cursor.getString(2);
                String strDate[] = cursor.getString(3).split("T");
                String oph_date = strDate[0].toString();
                int oph_status1 = cursor.getInt(4);
                int oph_status2 = cursor.getInt(5);
                String oph_status3 = cursor.getString(6);
                String oph_name = cursor.getString(7);
                String oph_status4 = "Total Asset "+intAssetExist+"/"+intAssetCount;

                OpnameHModel opnameHModel = new OpnameHModel(oph_id,
                        oph_title, oph_date, oph_status1, oph_status2, oph_status3, oph_name, oph_status4);
                returnList.add(opnameHModel);
            }while (cursor.moveToNext());

        }

        cursor.close();
        db.close();

        return returnList;
    }
}
