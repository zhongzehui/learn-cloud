package com.jeff.order.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class OrderVO implements Serializable {

    private static final long serialVersionUID = -5809782228272912349L;

    private Integer id;
    private String order_content;
    private Timestamp ts;

    public OrderVO() {
    }

    public OrderVO(String order_content, Timestamp ts) {
        this.order_content = order_content;
        this.ts = ts;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public Timestamp getTs() {
        return ts;
    }

    public void setTs(Timestamp ts) {
        this.ts = ts;
    }
}
