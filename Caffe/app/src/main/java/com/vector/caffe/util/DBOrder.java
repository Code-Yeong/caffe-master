package com.vector.caffe.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vector.caffe.bean.OrderItems;
import com.vector.caffe.db.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guo on 17-6-13.
 */

public class DBOrder implements DBBase<OrderItems> {
    private DatabaseHelper dh;
    private SQLiteDatabase database;
    private List<Map<String, String>> lists;

    public DBOrder(Context cxt) {
        dh = DatabaseHelper.getInstance(cxt);
        lists = new ArrayList<>();
    }

    @Override
    public List<Map<String, String>> queryAll() {
        return null;
    }

    public List<Map<String, String>> queryAllDetails() {
        List<Map<String, String>> lists = new ArrayList<>();
        Map<String, String> map;
        Cursor c1 = null, c2 = null, c3 = null;
        database = dh.getReadableDatabase();
        c1 = database.rawQuery("select * from corder o where o.ovalid='0' order by o.otime DESC", null);
        while (c1.moveToNext()) {
            c2 = database.rawQuery("select * from cdetail d where d.oid=" + c1.getString(c1.getColumnIndex("oid")), null);
            map = new HashMap<>();
            for (int i = 1; i < c2.getCount() + 1; i++) {
                c2.moveToNext();
                Log.i("info", c2.getString(c2.getColumnIndex("mid")));
                c3 = database.rawQuery("select * from cmenu m where m.mid='" + c2.getString(c2.getColumnIndex("mid")) + "'", null);
                Log.i("info", "count:" + c3.getCount());
                c3.moveToNext();
                map.put("time", c2.getString(c2.getColumnIndex("oid")));
                map.put("name" + i, c3.getString(c3.getColumnIndex("mname")));
                map.put("count" + i, c2.getString(c2.getColumnIndex("dcount")));
                map.put("note" + i, c2.getString(c2.getColumnIndex("dnote")));
                map.put("size" + i, c2.getString(c2.getColumnIndex("dsize")));
            }
            lists.add(map);
        }
        if (c3 != null)
            c3.close();
        if (c2 != null)
            c2.close();
        if (c1 != null)
            c1.close();
        database.close();
        dh.close();
        return lists;
    }

    @Override
    public List<Map<String, String>> queryByCloumn() {
        return null;
    }

    @Override
    public long insert(OrderItems orderItems) {
        database = dh.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("oid", orderItems.getOid());
        contentValues.put("omoney", orderItems.getOmoney());
        contentValues.put("ostaff", orderItems.getOstaff());
        contentValues.put("otime", orderItems.getOtime());
        contentValues.put("ovalid", "0");
        long result = database.insert("corder", null, contentValues);
        database.close();
        dh.close();
        return result;
    }

    @Override
    public long updateById(OrderItems orderItems) {
        return 0;
    }

    @Override
    public long deleteById(OrderItems orderItems) {
        return 0;
    }

    public String getNewOrderId() {
        String tail = "";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmm");
        String prefix = sdf.format(date);
        database = dh.getReadableDatabase();
        Cursor cursor = database.query("corder", null, "oid like '" + prefix.substring(0, 6) + "%'", new String[]{}, null, null, null);
        int count = cursor.getCount();
        if (count < 9) {
            tail = "00" + (count + 1);
        } else if (count < 99) {
            tail = "0" + (count + 1);
        } else {
            tail = "" + (count + 1);
        }
        database.close();
        dh.close();
        return prefix + tail;
    }
}
