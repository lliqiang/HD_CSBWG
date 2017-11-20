package com.hengda.smart.changsha.d.model;/**
 * Created by lenovo on 2017/10/9.
 */

/**
 * 创建人：lenovo
 * 创建时间：2017/10/9 17:10
 * 类描述：
 */
public class ResponseBeen {

    /**
     * status : 1
     * data : 0
     * msg : 操作成功
     */

    private int status;
    private int data;
    private String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
