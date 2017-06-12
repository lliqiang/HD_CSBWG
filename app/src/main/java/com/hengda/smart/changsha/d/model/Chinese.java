package com.hengda.smart.changsha.d.model;

import org.litepal.crud.DataSupport;

/**
 * Created by lenovo on 2017/3/10.
 */

public class Chinese extends DataSupport {

    /**
     * autonum : 1
     * exhibit_id : 0001
     * filename : Chinese/0001
     * byname : 序厅
     * subheading : 大家好！欢迎各位来到长沙博物馆参观，我们首先参观到的是《湘江北去——长沙古代史文
     * map_id : 1
     * img_num : 1
     * axis_x : 425
     * axis_y : 304
     */

    private int autonum;
    private String exhibit_id;
    private String filename;
    private String byname;
    private String subheading;
    private String map_id;
    private String img_num;
    private String axis_x;
    private String axis_y;

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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getByname() {
        return byname;
    }

    public void setByname(String byname) {
        this.byname = byname;
    }

    public String getSubheading() {
        return subheading;
    }

    public void setSubheading(String subheading) {
        this.subheading = subheading;
    }

    public String getMap_id() {
        return map_id;
    }

    public void setMap_id(String map_id) {
        this.map_id = map_id;
    }

    public String getImg_num() {
        return img_num;
    }

    public void setImg_num(String img_num) {
        this.img_num = img_num;
    }

    public String getAxis_x() {
        return axis_x;
    }

    public void setAxis_x(String axis_x) {
        this.axis_x = axis_x;
    }

    public String getAxis_y() {
        return axis_y;
    }

    public void setAxis_y(String axis_y) {
        this.axis_y = axis_y;
    }

    @Override
    public String toString() {
        return "Chinese{" +
                "autonum=" + autonum +
                ", exhibit_id='" + exhibit_id + '\'' +
                ", filename='" + filename + '\'' +
                ", byname='" + byname + '\'' +
                ", subheading='" + subheading + '\'' +
                ", map_id='" + map_id + '\'' +
                ", img_num='" + img_num + '\'' +
                ", axis_x='" + axis_x + '\'' +
                ", axis_y='" + axis_y + '\'' +
                '}';
    }
}
