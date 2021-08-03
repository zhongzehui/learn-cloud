package com.jeff.order.rabbitmq.controller;



import com.rabbitmq.client.*;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ConsumerController {

    @RequestMapping("/consume")
    public String consume() {

        //1创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.60.129");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setVirtualHost("/");

        // 2创建连接connection
        Connection connection = null;
        Channel channel = null;

        try {
            connection = connectionFactory.newConnection("消费者");
            //通过连接获取通道channel
            channel = connection.createChannel();
//        通过创建交换机，声明队列，绑定关系，路由key，发送消息和接受消息
            String queueName = "queue1" ;
            final  Channel temp = channel;
            String rec = channel.basicConsume(queueName, true , new DeliverCallback(){
                @Override
                public void handle(String consumerTag, Delivery message) throws IOException {
                    System.out.println("consumerTag = "+ consumerTag);
                    System.out.println("message = "+ StringUtils.newStringUtf8(message.getBody()));
                    temp.basicAck(message.getEnvelope().getDeliveryTag(),false);

                }
            },new CancelCallback(){
                @Override
                public void handle(String consumerTag) throws IOException {

                }
            });
          //  GetResponse getResponse = channel.basicGet(queueName, true);

           // System.out.println(StringUtils.newStringUtf8(getResponse.getBody()));
           // String message = "hello world!";
            //channel.basicPublish("", queueName, null, message.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (connection != null && connection.isOpen()) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

//                准备消息内容
//        贮备发送消息到队列
//        关闭连接
//        关闭通道
        return "success";
    }
}
