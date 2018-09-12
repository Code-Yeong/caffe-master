package com.vector.caffe.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vector.caffe.bean.MenuItems;
import com.vector.caffe.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guo on 17-6-13.
 */

public class DBMenu implements DBBase<MenuItems> {

    private DatabaseHelper dh;
    private SQLiteDatabase database;
    private List<Map<String, String>> lists;

    public DBMenu(Context cxt) {
        dh = DatabaseHelper.getInstance(cxt);
        lists = new ArrayList<>();
    }

    @Override
    public List<Map<String, String>> queryAll() {
        database = dh.getReadableDatabase();
        lists.clear();
        Cursor cursor = database.query("cmenu", null, null, null, null, null, "mid ASC");

        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<>();
            map.put("id", cursor.getInt(cursor.getColumnIndex("id")) + "");
            map.put("mid", cursor.getString(cursor.getColumnIndex("mid")));
            map.put("mname", cursor.getString(cursor.getColumnIndex("mname")));
            map.put("mprice", cursor.getString(cursor.getColumnIndex("mprice")));
            map.put("mcategory", cursor.getString(cursor.getColumnIndex("mcategory")));
            map.put("mvalid", cursor.getString(cursor.getColumnIndex("mvalid")));
            lists.add(map);
        }
        return lists;
    }

    @Override
    public List<Map<String, String>> queryByCloumn() {
        return null;
    }

    @Override
    public long insert(MenuItems menuItems) {
        long result = -1;
        database = dh.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", menuItems.getId());
        contentValues.put("mid", menuItems.getMid());
        contentValues.put("mname", menuItems.getMname());
        contentValues.put("mcategory", menuItems.getMcategory());
        contentValues.put("mprice", menuItems.getMprice());
        contentValues.put("mvalid", "1");

        result = database.insert("cmenu", null, contentValues);

        dh.close();
        database.close();
        return result;
    }

    @Override
    public long updateById(MenuItems menuItems) {
        return 0;
    }

    @Override
    public long deleteById(MenuItems menuItems) {
        long result = -1;
        database = dh.getWritableDatabase();
        result = database.delete("cmenu", "id=?", new String[]{menuItems.getId()});
        database.close();
        dh.close();
        return result;
    }

    public List<Map<String, String>> queryByName(String name) {
        lists.clear();
        database = dh.getReadableDatabase();
        Cursor cursor = database.query("cmenu", null, "mname like '%" + name + "%' or mid like '%" + name + "%'", null, null, null, "mid ASC");

        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<>();
            map.put("id", cursor.getInt(cursor.getColumnIndex("id")) + "");
            map.put("mid", cursor.getString(cursor.getColumnIndex("mid")));
            map.put("mname", cursor.getString(cursor.getColumnIndex("mname")));
            map.put("mprice", cursor.getString(cursor.getColumnIndex("mprice")));
            map.put("mcategory", cursor.getString(cursor.getColumnIndex("mcategory")));
            map.put("mvalid", cursor.getString(cursor.getColumnIndex("mvalid")));
            lists.add(map);
        }
        cursor.close();
        database.close();
        dh.close();
        return lists;
    }

    public String getCountId(String prefix) {
        database = dh.getReadableDatabase();
        String id;
        Cursor cursor = database.query("cmenu", null, "mid like '" + prefix + "%'", null, null, null, "mid ASC");

        int count = cursor.getCount() + 1;
        if (count < 10) {
            id = "0" + count;
        } else {
            id = "" + count;
        }
        cursor.close();
        database.close();
        dh.close();
        return id;
    }
}
