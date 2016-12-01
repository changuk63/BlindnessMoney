package com.example.leechanguk.blindnessmoney;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Leechanguk on 2016-11-30.
 */

public class LoanDBOpenHelper {
    private final String DATABASE_NAME = "Loanlist.db";
    private final int DATABASE_VERSION = 1;
    private final String TABLENAME = "LOAN";
    public static SQLiteDatabase mDB;
    private LoanDBHelper mDBHelper;
    private Context mCtx;

    private class LoanDBHelper extends SQLiteOpenHelper {
        public LoanDBHelper(Context ctx, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(ctx, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLENAME + "(id integer primary key autoincrement, "
                    + "name string not null , time string not null , payment string not null , content string);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public LoanDBOpenHelper(Context context){
        this.mCtx = context;
    }

    public LoanDBOpenHelper open() throws SQLException{
        mDBHelper = new LoanDBHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void add(String target, String date, String payment, String content){
        mDB.execSQL("INSERT INTO LOAN(name, time, payment, content) VALUES ('" + target +"', '" + date + "', '" + payment + "', '"+ content +"');");
    }

    public void remove(String name){
        mDB.execSQL("DELETE FROM LOAN WHERE name = '"+ name +"';");
    }

    public int size(){
        Cursor cursor = mDB.rawQuery("SELECT id FROM LOAN;", null);
        return cursor.getCount();
    }

    public Cursor searchTable(){
        return mDB.rawQuery("SELECT * FROM LOAN",null);
    }

    public void close(){
        mDB.close();
    }
}
