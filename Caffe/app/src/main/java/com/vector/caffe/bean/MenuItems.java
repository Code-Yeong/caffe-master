package com.vector.caffe.bean;

/**
 * Created by guo on 17-6-13.
 */

public class MenuItems {
    private String id;
    private String mid;
    private String mname;
    private String mcategory;
    private String mprice;
    private String mvalid;

    public MenuItems(String id) {
        this.id = id;
    }

    public MenuItems(String mname, String mid, String mcategory, String mprice) {
        this.mname = mname;
        this.mid = mid;
        this.mcategory = mcategory;
        this.mprice = mprice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getMcategory() {
        return mcategory;
    }

    public void setMcategory(String mcategory) {
        this.mcategory = mcategory;
    }

    public String getMprice() {
        return mprice;
    }

    public void setMprice(String mprice) {
        this.mprice = mprice;
    }

    public String getMvalid() {
        return mvalid;
    }

    public void setMvalid(String mvalid) {
        this.mvalid = mvalid;
    }
}
