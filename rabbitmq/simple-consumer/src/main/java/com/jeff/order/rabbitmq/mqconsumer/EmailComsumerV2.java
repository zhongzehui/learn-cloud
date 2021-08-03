package com.jeff.order.rabbitmq.mqconsumer;


import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;


/*
    使用注解来定义消费者
 */

//@RabbitListener( queues = {"fanout_queue_v2"})
@RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "annotation_email_queue", durable = "true", autoDelete = "false"),
         exchange = @Exchange(value = "annoutation_topic_Exchange", type = "topic" ),
        key= {"#.email.#"}
))
public class EmailComsumerV2 {

    @RabbitHandler
    public void reviceMessage(String strMessage, Object msg, Message message, Channel channel){
        System.out.println("MessageComsumerV2## reviceMessage 接受到消息--->" + strMessage);
    }
}
