package com.yonyou.mqservice.controller;


import com.yonyou.mqservice.service.OrderService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ProducerContorller {





    @RequestMapping("/produce")
    public String produceMsg() {

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
            connection = connectionFactory.newConnection("生产者");
            //通过连接获取通道channel
            channel = connection.createChannel();
//        通过创建交换机，声明队列，绑定关系，路由key，发送消息和接受消息
            String queueName = "queue1";

            String message = "hello world!";
            //交换机名称
            String exchangeName = "";
            //交换机类型
            String type= "";
            //路由地址
            String routingKey = "";
            //维护交换机信息与队列信息
            //声明交换机
            channel.exchangeDeclare(exchangeName, type);
            //声明队列
            channel.queueDeclare(queueName, true, false, false, null);
            //绑定队列与交换机
            channel.queueBind(queueName, exchangeName, routingKey);
            //声明队列
            //defaultMode(channel, queueName, message, exchangeName);
            usualMode(channel,exchangeName, routingKey, message);

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


    @Autowired
    private OrderService orderService;

    @RequestMapping("makeOrder")
    public String makeOrder(){
        orderService.makeOrder("zhangsan", "nintendo switch ",  2);
        return "suucess";
    }
    @RequestMapping("addCart")
    public String addCart(){
        orderService.addCart("lisi", "nokia  ",  10);

        return "suucess";
    }
    @RequestMapping("addCart4Special")
    public String addCart4Special(){
        orderService.addCart4Special("lisi", "nokia  ",  10);

        return "suucess";
    }
    /**
     *
     * @param channel 队列名称
     * @param queueName  队列名称
     * @param message  消息内容
     * @param exchangeName  交换机名称
     * @throws IOException
     */
    private void defaultMode(Channel channel, String queueName, String message, String exchangeName) throws IOException {
        /*
         *@params
         *@params
         *@params 排他性
         *@params 是否自动参数
         *@params  附带附属参数
         */
        channel.queueDeclare(queueName,  true, false, false, null);
        channel.basicPublish(exchangeName, queueName, null, message.getBytes());
    }
    /**
     * 非默认情况发送
     * @param channel 队列名称
     * @param exchangeName  交换机名称
     * @param routingKey  路由
     * @param message  消息
     * @throws IOException
     */
    private void usualMode(Channel channel, String exchangeName, String routingKey, String message) throws IOException {
        //使用不同的模式，需要先维护交换机信息和队列信息
        //然后绑定对应的交换机与队列
        channel.basicPublish(exchangeName, routingKey, null, message.getBytes());
    }
}
