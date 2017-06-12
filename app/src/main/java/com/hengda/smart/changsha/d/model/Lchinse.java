package com.hengda.smart.changsha.d.model;

import android.database.Cursor;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/5/13.
 */

public class Lchinse extends DataSupport implements Serializable{

    /**
     * autonum : 1
     * exhibit_id : 0001
     * byname : 123123
     */

    private int autonum;
    private String exhibit_id;
    private String byname;

    public int getAutonum() {
        return autonum;
    }

    public void setAutonum(int autonum) {
        this.autonum = autonum;
    }

    public String getExhibit_id() {
        return exhibit_id;
    }

    public void setExhibit_id(String exhibit_id) {
        this.exhibit_id = exhibit_id;
    }

    public String getByname() {
        return byname;
    }

    public void setByname(String byname) {
        this.byname = byname;
    }

    public static Lchinse CursorToModel(Cursor cursor) {
        Lchinse exhibition = new Lchinse();
        exhibition.setByname(cursor.getString(cursor.getColumnIndex("byname")));
        exhibition.setExhibit_id(cursor.getString(cursor.getColumnIndex("exhibit_id")));
        exhibition.setAutonum(cursor.getInt(cursor.getColumnIndex("autonum")));
        return exhibition;
    }
}
