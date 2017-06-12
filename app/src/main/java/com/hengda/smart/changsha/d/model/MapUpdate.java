package com.hengda.smart.changsha.d.model;

import java.util.List;

/**
 * Created by lenovo on 2017/3/13.
 */

public class MapUpdate {


    /**
     * status : 1
     * data : [{"name":"古代历史文化一厅","path":"http://192.168.10.20/csbwg/data/upload/1","md5":"1","img":"http://192.168.10.20/csbwg/data/upload/project/20170313/58c60130b17e4.png","map_id":"1","language":"1"},{"name":"1","path":"http://192.168.10.20/csbwg/data/upload/1","md5":"1","img":"http://192.168.10.20/csbwg/data/upload/1","map_id":"1","language":"2"},{"name":"1","path":"http://192.168.10.20/csbwg/data/upload/1","md5":"1","img":"http://192.168.10.20/csbwg/data/upload/1","map_id":"1","language":"3"},{"name":"1","path":"http://192.168.10.20/csbwg/data/upload/1","md5":"1","img":"http://192.168.10.20/csbwg/data/upload/1","map_id":"1","language":"4"},{"name":"2","path":"http://192.168.10.20/csbwg/data/upload/2","md5":"2","img":"http://192.168.10.20/csbwg/data/upload/2","map_id":"2","language":"1"},{"name":"2","path":"http://192.168.10.20/csbwg/data/upload/2","md5":"2","img":"http://192.168.10.20/csbwg/data/upload/2","map_id":"2","language":"2"},{"name":"2","path":"http://192.168.10.20/csbwg/data/upload/2","md5":"2","img":"http://192.168.10.20/csbwg/data/upload/2","map_id":"2","language":"3"},{"name":"2","path":"http://192.168.10.20/csbwg/data/upload/2","md5":"2","img":"http://192.168.10.20/csbwg/data/upload/2","map_id":"2","language":"4"},{"name":"3","path":"http://192.168.10.20/csbwg/data/upload/3","md5":"3","img":"http://192.168.10.20/csbwg/data/upload/3","map_id":"3","language":"1"},{"name":"3","path":"http://192.168.10.20/csbwg/data/upload/3","md5":"3","img":"http://192.168.10.20/csbwg/data/upload/3","map_id":"3","language":"2"},{"name":"3","path":"http://192.168.10.20/csbwg/data/upload/3","md5":"3","img":"http://192.168.10.20/csbwg/data/upload/3","map_id":"3","language":"3"},{"name":"3","path":"http://192.168.10.20/csbwg/data/upload/3","md5":"3","img":"http://192.168.10.20/csbwg/data/upload/3","map_id":"3","language":"4"},{"name":"4","path":"http://192.168.10.20/csbwg/data/upload/4","md5":"4","img":"http://192.168.10.20/csbwg/data/upload/4","map_id":"4","language":"1"},{"name":"4","path":"http://192.168.10.20/csbwg/data/upload/4","md5":"4","img":"http://192.168.10.20/csbwg/data/upload/4","map_id":"4","language":"2"},{"name":"4","path":"http://192.168.10.20/csbwg/data/upload/4","md5":"4","img":"http://192.168.10.20/csbwg/data/upload/4","map_id":"4","language":"3"},{"name":"4","path":"http://192.168.10.20/csbwg/data/upload/4","md5":"4","img":"http://192.168.10.20/csbwg/data/upload/4","map_id":"4","language":"4"}]
     * msg : 查询成功
     */

//    private int status;
//    private String msg;
    /**
     * name : 古代历史文化一厅
     * path : http://192.168.10.20/csbwg/data/upload/1
     * md5 : 1
     * img : http://192.168.10.20/csbwg/data/upload/project/20170313/58c60130b17e4.png
     * map_id : 1
     * language : 1
     */

    private List<DataBean> data;

//    public int getStatus() {
//        return status;
//    }
//
//    public void setStatus(int status) {
//        this.status = status;
//    }

//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String name;
        private String path;
        private String md5;
        private String img;
        private String map_id;
        private String language;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
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
}
