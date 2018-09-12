package com.vector.caffe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by guo on 17-6-13.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DBNAME = "caffe";
    private static final int VERSION = 1;

    private static final String TABLE_MENU = "cmenu";
    private static final String TABLE_ORDER = "corder";
    private static final String TABLE_DETAIL = "cdetail";

    private static DatabaseHelper dh;

    private Context cxt;

    private String CREATE_MENU = "create table cmenu(" +
            "id integer primary key autoincrement," +
            "mid varchar(20)," +
            "mname varchar(20)," +
            "mprice varchar(10) default 0," +
            "mvalid varchar(2) default 1," +
            "mcategory varchar(10));";

    private String CREATE_ORDER = "create table corder(" +
            "id integer primary key autoincrement," +
            "oid varchar(20)," +
            "otime varchar(20)," +
            "omoney varchar(20)," +
            "ovalid varchar(5)," +
            "ostaff varchar(20));";

    private String CREATE_DETAIL = "create table cdetail(" +
            "id integer primary key autoincrement," +
            "mid varchar(20)," +
            "oid varchar(20)," +
            "dprice varchar(10)," +
            "dcount varchar(5)," +
            "dsize varchar(5)," +
            "dtemp varchar(5)," +
            "dnote varchar(30));";

    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, VERSION);
        this.cxt = context;
    }

    public static synchronized DatabaseHelper getInstance(Context cxt) {
        if (dh == null) {
            dh = new DatabaseHelper(cxt);
        }
        return dh;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MENU);
        db.execSQL(CREATE_ORDER);
        db.execSQL(CREATE_DETAIL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

}
