package com.vector.caffe.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.vector.caffe.bean.DetailItems;
import com.vector.caffe.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by guo on 17-6-13.
 */

public class DBDetail implements DBBase<DetailItems> {
    private DatabaseHelper dh;
    private SQLiteDatabase database;
    private List<Map<String, String>> lists;

    public DBDetail(Context cxt) {
        dh = DatabaseHelper.getInstance(cxt);
        lists = new ArrayList<>();
    }

    @Override
    public List<Map<String, String>> queryAll() {
        return null;
    }

    @Override
    public List<Map<String, String>> queryByCloumn() {
        return null;
    }

    @Override
    public long insert(DetailItems detailItems) {
        return 0;
    }

    public long insertAll(List<DetailItems> datas) {
        database = dh.getWritableDatabase();
        for (DetailItems d : datas) {
            ContentValues values = new ContentValues();
            values.put("oid", d.getOid());
            values.put("mid", d.getMid());
            values.put("dsize", d.getDsize());
            values.put("dprice", d.getDprice());
            values.put("dtemp", d.getDtemp());
            values.put("dnote", d.getDnote());
            values.put("dcount", d.getDcount());
            if (database.insert("cdetail", null, values) < 1) {
                return -1;
            }
        }
        return 1;
    }

    @Override
    public long updateById(DetailItems detailItems) {
        return 0;
    }

    @Override
    public long deleteById(DetailItems detailItems) {
        return 0;
    }
}
