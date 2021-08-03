package com.zehui.rabbitmq;


import com.rabbitmq.client.*;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class RabbitMQTestDemo {
    private String queueName = "queue1";

    //交换机名称
    private String exchangeName = "";
    //交换机类型
    private String type = "";
    //路由地址
    private String routingKey = "";

    @Test
    public void producerTest() {
        init();
        String message = "hello world!";
        try {
            channel.basicPublish(exchangeName, queueName, null, message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void ConsumerTest() {
        init();
        try {
            final Channel temp = channel;
            channel.basicConsume(queueName, true, (consumerTag, message) ->
                    {
                        System.out.println("consumerTag = " + consumerTag);
                        System.out.println("message = " + StringUtils.newStringUtf8(message.getBody()));
                        // 如果接受消息的时候设置autoAck自动确认是false，则需要执行以下代码，确认消息接受
                        //temp.basicAck(message.getEnvelope().getDeliveryTag(),false);
                    }
                    , (consumerTag) -> {
                        //执行接受失败的操作
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Connection connection = null;
    private Channel channel = null;

    private void init() {
        //1创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.60.129");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setVirtualHost("/");

        try {
            // 2创建连接connection
            connection = connectionFactory.newConnection("生产者");
            //3.通过连接获取通道channel
            channel = connection.createChannel();
//        通过创建交换机，声明队列，绑定关系，路由key，发送消息和接受消息

            //维护交换机信息与队列信息
            //声明交换机
            channel.exchangeDeclare(exchangeName, type);
            //声明队列
            channel.queueDeclare(queueName, true, false, false, null);
            //绑定队列与交换机
            channel.queueBind(queueName, exchangeName, routingKey);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //        关闭通道
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //        关闭连接
            if (connection != null && connection.isOpen()) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
