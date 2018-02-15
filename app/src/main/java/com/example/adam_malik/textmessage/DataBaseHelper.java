package com.example.adam_malik.textmessage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Adam_Malik on 2/15/18.
 */

class DataBaseHelper extends SQLiteOpenHelper{
    private static final String DB_NAME = "messagedb";

    public DataBaseHelper(Context context) {
        super(context,DB_NAME,null,1);
    }

//    public SQLiteDatabase getWritableDatabase() {
//        SQLiteDatabase writableDatabase = new SQLiteDatabase();
//        return writableDatabase;
    //}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE SMS (ADDRESS VARCHAR(34), DATE VARCHAR(20), BODY VARCHAR(100))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
