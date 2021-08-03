package com.jeff.order.rabbitmq.mqconsumer;


import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class MessageComsumer {

    @RabbitListener(queues = "message_queue")
    public void receive_message(Object msg, Message message, Channel channel){

        System.out.println(" 从队列获取消息成功 message_queue =====> "+new String(message.getBody()).toString());
    }


    @RabbitListener(queues = "email_queue")
    public void receive_email(Object msg, Message message, Channel channel){
        System.out.println(" 从队列获取消息成功 email_queue =====> "+new String(message.getBody()).toString());
    }


    @RabbitListener(queues = "wechat_queue")
    public void receive_wechat(Object msg, Message message, Channel channel){
        System.out.println(" 从队列获取消息成功  email_queue =====> "+ new String(message.getBody()).toString());
    }

    @RabbitListener(queues = "points_queue")
    public void receive_points(Object msg, Message message, Channel channel){
        System.out.println(" 从队列获取消息成功  email_queue =====> "+ new String(message.getBody()).toString() );
    }

}
