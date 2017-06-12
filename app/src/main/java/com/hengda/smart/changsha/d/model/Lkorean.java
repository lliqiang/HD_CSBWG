package com.hengda.smart.changsha.d.model;

import org.litepal.crud.DataSupport;

/**
 * Created by lenovo on 2017/5/13.
 */

public class Lkorean extends DataSupport {

    /**
     * autonum : 1
     * exhibit_id : 0001
     * byname : 234234
     */

    private String autonum;
    private String exhibit_id;
    private String byname;

    public String getAutonum() {
        return autonum;
    }

    public void setAutonum(String autonum) {
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
}
