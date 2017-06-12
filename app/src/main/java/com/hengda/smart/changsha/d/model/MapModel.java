package com.hengda.smart.changsha.d.model;

import android.database.Cursor;

import org.litepal.crud.DataSupport;

/**
 * Created by lenovo on 2017/3/13.
 */

public class MapModel extends DataSupport {

    /**
     * name : 古代历史文化一厅
     * img : http://192.168.10.20/csbwg/data/upload/project/20170314/58c74cf52cfd2.png
     * map_id : 1
     * language : 1
     */

    private String name;
    private String img;
    private String map_id;
    private String language;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getMap_id() {
        return map_id;
    }

    public void setMap_id(String map_id) {
        this.map_id = map_id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public static MapModel CursorToMap(Cursor cursor) {
        MapModel mapModel = new MapModel();
        mapModel.setLanguage(cursor.getString(2));
        mapModel.setMap_id(cursor.getString(3));
        mapModel.setName(cursor.getString(4));
        return mapModel;
    }

    @Override
    public String toString() {
        return "MapModel{" +
                "name='" + name + '\'' +
                ", img='" + img + '\'' +
                ", map_id='" + map_id + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
