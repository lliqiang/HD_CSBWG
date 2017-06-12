package com.hengda.smart.changsha.d.model;

/**
 * Created by lenovo on 2017/3/13.
 */

public class TcpResp {
    private String type;
    private String client_id;
    private String send_type;
    private String send_content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getSend_type() {
        return send_type;
    }

    public void setSend_type(String send_type) {
        this.send_type = send_type;
    }

    public String getSend_content() {
        return send_content;
    }

    public void setSend_content(String send_content) {
        this.send_content = send_content;
    }

    @Override
    public String toString() {
        return "TcpResp{" +
                "type='" + type + '\'' +
                ", client_id='" + client_id + '\'' +
                ", send_type='" + send_type + '\'' +
                ", send_content='" + send_content + '\'' +
                '}';
    }
}
