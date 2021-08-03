package com.yonyou.mqservice.service;


import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void makeOrder(String userId ,String productId, Integer num){


        String exchangeName = "topic_Exchange";
        String routingKey = "com.zehui.order.makeborder";
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("userId", userId);
        dataMap.put("productId", productId);
        dataMap.put("num", num);
        String message = dataMap.toString();
        //执行发送
        rabbitTemplate.convertAndSend(exchangeName,routingKey,message.getBytes());


    }


    public void addCart(String userId ,String productId, Integer num){
        String exchangeName = "fanout_Exchange";
        String routingKey = "";
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("userId", userId);
        dataMap.put("productId", productId);
        dataMap.put("num", num);
        String message = dataMap.toString();
        //执行发送
        rabbitTemplate.convertAndSend(exchangeName,routingKey,message.getBytes());
        //发送带过期时间的消息
        MessagePostProcessor postProcessor = (postmessage)->{
            // 消息处理器
            postmessage.getMessageProperties().setExpiration("10000");
            postmessage.getMessageProperties().setContentEncoding("UTF-8");
            return postmessage;
        };
      //  rabbitTemplate.convertAndSend(exchangeName,routingKey,message,postProcessor);
        rabbitTemplate.convertAndSend(exchangeName,routingKey,message);

    }
    public void addCart4Special(String userId ,String productId, Integer num){
        String exchangeName = "topic_exchange";
        String routingKey = "";
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("userId", userId);
        dataMap.put("productId", productId);
        dataMap.put("num", num);
        String message = dataMap.toString();

        //发送带过期时间的消息
        MessagePostProcessor postProcessor = (postmessage)->{
            // 消息处理器
            postmessage.getMessageProperties().setExpiration("7000"); //  7m过期
            postmessage.getMessageProperties().setContentEncoding("UTF-8");
            return postmessage;
        };
        rabbitTemplate.convertAndSend(exchangeName,routingKey,message,postProcessor);

    }


    private void test(){
        //发送到默认的交换机
        rabbitTemplate.convertAndSend("");
    }



}
