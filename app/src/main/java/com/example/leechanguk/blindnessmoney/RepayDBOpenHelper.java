package com.example.leechanguk.blindnessmoney;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Leechanguk on 2016-11-30.
 */

public class RepayDBOpenHelper {
    private final String DATABASE_NAME = "Repaylist.db";
    private final int DATABASE_VERSION = 1;
    private final String TABLENAME = "REPAY";
    public static SQLiteDatabase mDB;
    private RepayDBHelper mDBHelper;
    private Context mCtx;

    private class RepayDBHelper extends SQLiteOpenHelper {
        public RepayDBHelper(Context ctx, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(ctx, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLENAME + "(id integer primary key autoincrement, "
                    + "name string not null , time string not null , payment string not null, content string);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public RepayDBOpenHelper(Context context){
        this.mCtx = context;
    }

    public RepayDBOpenHelper open() throws SQLException {
        mDBHelper = new RepayDBHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void remove(String name){
        mDB.execSQL("DELETE FROM REPAY WHERE name = '"+ name +"';");
    }

    public void add(String target, String date, String payment, String content){
        mDB.execSQL("INSERT INTO REPAY(name, time, payment, content) VALUES ('" + target +"', '" + date + "', '" + payment + "', '"+ content +"');");
    }

    public int size(){
        Cursor cursor = mDB.rawQuery("SELECT id FROM REPAY;", null);
        return cursor.getCount();
    }

    public Cursor searchTable(){
        return mDB.rawQuery("SELECT * FROM REPAY",null);
    }

    public void close(){
        mDB.close();
    }
}
