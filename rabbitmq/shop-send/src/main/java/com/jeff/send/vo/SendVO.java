package com.jeff.send.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class SendVO implements Serializable {

    private static final long serialVersionUID = -5809782578272912349L;

    private Integer id;
    private String order_content;
    private String send_content;
    private Timestamp ts;

    public SendVO() {
    }

    public SendVO( String order_content, String send_content, Timestamp ts) {
        this.order_content = order_content;
        this.send_content = send_content;
        this.ts = ts;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrder_content() {
        return order_content;
    }

    public void setOrder_content(String order_content) {
        this.order_content = order_content;
    }

    public String getSend_content() {
        return send_content;
    }

    public void setSend_content(String send_content) {
        this.send_content = send_content;
    }

    public Timestamp getTs() {
        return ts;
    }

    public void setTs(Timestamp ts) {
        this.ts = ts;
    }
}
