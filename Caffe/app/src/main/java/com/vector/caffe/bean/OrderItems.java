package com.vector.caffe.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by guo on 17-6-12.
 */

public class OrderItems {
    private String oid;
    private String omoney;
    private String ostaff;
    private String otime;
    private String ovalid;

    public OrderItems(String oid, String omoney, String ostaff) {
        this.oid = oid;
        this.omoney = omoney;
        this.ostaff = ostaff;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getOmoney() {
        return omoney;
    }

    public void setOmoney(String omoney) {
        this.omoney = omoney;
    }

    public String getOstaff() {
        return ostaff;
    }

    public void setOstaff(String ostaff) {
        this.ostaff = ostaff;
    }

    public String getOtime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(new Date());
    }

    public void setOtime(String otime) {
        this.otime = otime;
    }

    public String getOvalid() {
        return ovalid;
    }

    public void setOvalid(String ovalid) {
        this.ovalid = ovalid;
    }
}
