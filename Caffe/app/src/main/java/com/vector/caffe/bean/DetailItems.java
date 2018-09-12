package com.vector.caffe.bean;

/**
 * Created by guo on 17-6-13.
 */

public class DetailItems {
    private String oid;
    private String mid;
    private String dprice;
    private String dcount;
    private String dsize;
    private String dtemp;
    private String dnote;

    public DetailItems(String oid, String mid, String dprice, String dcount, String dsize, String dtemp, String dnote) {
        this.oid = oid;
        this.mid = mid;
        this.dprice = dprice;
        this.dcount = dcount;
        this.dsize = dsize;
        this.dtemp = dtemp;
        this.dnote = dnote;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getDprice() {
        return dprice;
    }

    public void setDprice(String dprice) {
        this.dprice = dprice;
    }

    public String getDcount() {
        return dcount;
    }

    public void setDcount(String dcount) {
        this.dcount = dcount;
    }

    public String getDsize() {
        return dsize;
    }

    public void setDsize(String dsize) {
        this.dsize = dsize;
    }

    public String getDtemp() {
        return dtemp;
    }

    public void setDtemp(String dtemp) {
        this.dtemp = dtemp;
    }

    public String getDnote() {
        return dnote;
    }

    public void setDnote(String dnote) {
        this.dnote = dnote;
    }
}
