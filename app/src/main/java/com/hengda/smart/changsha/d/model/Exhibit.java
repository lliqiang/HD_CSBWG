package com.hengda.smart.changsha.d.model;

import android.database.Cursor;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/3/7.
 */

public class Exhibit implements Serializable {
    private String UnitName;
    private int UnitNO;
    private String UnitPath;
    private int Floor;

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String unitName) {
        UnitName = unitName;
    }

    public int getUnitNO() {
        return UnitNO;
    }

    public void setUnitNO(int unitNO) {
        UnitNO = unitNO;
    }

    public String getUnitPath() {
        return UnitPath;
    }

    public void setUnitPath(String unitPath) {
        UnitPath = unitPath;
    }

    public int getFloor() {
        return Floor;
    }

    public void setFloor(int floor) {
        Floor = floor;
    }
    public static Exhibit getModelFromCursor(Cursor cursor){
        Exhibit exhibit=new Exhibit();
        exhibit.setUnitName(cursor.getString(0));
        exhibit.setUnitNO(cursor.getInt(1));
        exhibit.setUnitPath(cursor.getString(2));
        exhibit.setFloor(cursor.getInt(3));
        return exhibit;
    }
}
