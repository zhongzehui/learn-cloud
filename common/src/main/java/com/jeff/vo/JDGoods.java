package com.jeff.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class JDGoods implements Serializable {

    private static final long serialVersionUID = 1111231231L;
    private String goodsName;
    private String img;
    private String price;

    public JDGoods() {
    }

    public JDGoods(String goodsName, String img, String price) {
        this.goodsName = goodsName;
        this.img = img;
        this.price = price;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "JDGoods{" +
                "goodsName='" + goodsName + '\'' +
                ", img='" + img + '\'' +
                ", price=" + price +
                '}';
    }
}
