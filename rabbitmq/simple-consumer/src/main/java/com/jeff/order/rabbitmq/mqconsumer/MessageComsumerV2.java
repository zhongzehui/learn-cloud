package com.jeff.order.rabbitmq.mqconsumer;


import org.springframework.amqp.rabbit.annotation.*;


/*
    使用注解来定义消费者
 */

//@RabbitListener( queues = {"fanout_queue_v2"})
@RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "annotation_message_queue", durable = "true", autoDelete = "false"),
         exchange = @Exchange(value = "annoutation_topic_Exchange", type = "topic"),
        key= {"#.messgae.#"}
))
public class MessageComsumerV2 {

    @RabbitHandler
    public void reviceMessage(String message){
        System.out.println("MessageComsumerV2## reviceMessage 接受到消息--->" + message);
    }
}
