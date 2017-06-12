package com.hengda.smart.changsha.d.model;

import android.database.Cursor;

import java.io.Serializable;

/**
 * Created by user on 2017/3/6.
 */

public class Exhibition implements Serializable {
    private int autonum;
    private int axis_x;
    private int axis_y;
    private String byname;
    private String exhibit_id;
    private String filename;
    private int map_id;
    private String UnitName;

    public int getAutonum() {
        return autonum;
    }

    public void setAutonum(int autonum) {
        this.autonum = autonum;
    }

    public int getAxis_x() {
        return axis_x;
    }

    public void setAxis_x(int axis_x) {
        this.axis_x = axis_x;
    }

    public int getAxis_y() {
        return axis_y;
    }

    public void setAxis_y(int axis_y) {
        this.axis_y = axis_y;
    }

    public String getByname() {
        return byname;
    }

    public void setByname(String byname) {
        this.byname = byname;
    }

    public String getExhibit_id() {
        return exhibit_id;
    }

    public void setExhibit_id(String exhibit_id) {
        this.exhibit_id = exhibit_id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getMap_id() {
        return map_id;
    }

    public void setMap_id(int map_id) {
        this.map_id = map_id;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String unitName) {
        UnitName = unitName;
    }

    public static Exhibition CursorToModel(Cursor cursor) {
        Exhibition exhibition = new Exhibition();
        exhibition.setAxis_x(cursor.getInt(cursor.getColumnIndex("axis_x")));
        exhibition.setAxis_y(cursor.getInt(cursor.getColumnIndex("axis_y")));
        exhibition.setByname(cursor.getString(cursor.getColumnIndex("byname")));
        exhibition.setExhibit_id(cursor.getString(cursor.getColumnIndex("exhibit_id")));
        exhibition.setFilename(cursor.getString(cursor.getColumnIndex("filename")));
        exhibition.setMap_id(cursor.getInt(cursor.getColumnIndex("map_id")));
        exhibition.setAutonum(cursor.getInt(cursor.getColumnIndex("autonum")));

        return exhibition;
    }

    @Override
    public String toString() {
        return "Exhibition{" +
                "autonum=" + autonum +
                ", axis_x=" + axis_x +
                ", axis_y=" + axis_y +
                ", byname='" + byname + '\'' +
                ", exhibit_id='" + exhibit_id + '\'' +
                ", filename='" + filename + '\'' +
                ", map_id=" + map_id +
                ", UnitName='" + UnitName + '\'' +
                '}';
    }
}
