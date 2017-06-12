package com.hengda.smart.changsha.d.model;

import org.litepal.crud.DataSupport;

/**
 * Created by lenovo on 2017/3/14.
 */

public class Map_info extends DataSupport {

    /**
     * name : 古代历史文化一厅
     * img : http://192.168.10.20/csbwg/data/upload/project/20170313/58c62f7b19cb9.png
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
}
