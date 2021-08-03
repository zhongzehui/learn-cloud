package com.jeff.order;

import com.alibaba.fastjson.JSONObject;
import com.jeff.order.vo.OrderVO;

import java.sql.Timestamp;

public class Test {
    public static void main(String[] args) {
        long mills = System.currentTimeMillis();
        System.out.println(mills);
        OrderVO orderVO = new OrderVO("content", new Timestamp( mills ) );
        String jsonStr = JSONObject.toJSONString(orderVO);
        System.out.println(jsonStr);
    }
}
