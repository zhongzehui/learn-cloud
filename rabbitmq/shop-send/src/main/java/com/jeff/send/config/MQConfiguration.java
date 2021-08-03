package com.jeff.send.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MQConfiguration {

    public static final String TOPIC_ORDER_EXCHANGE = "topic_order_exchange";
    public static final String ORDER_SEND_QUEUE = "order_send_queue";


    @Bean(TOPIC_ORDER_EXCHANGE)
    public TopicExchange topic_order_Exchange() {
        return new TopicExchange(TOPIC_ORDER_EXCHANGE, true, false);
    }

    //注册队列
    @Bean(ORDER_SEND_QUEUE)
    public Queue order_send_queue() {

        Map<String, Object> args = new HashMap<>();
        int ttl = 6_000; //设置队列消息过期时间
        args.put("x-message-ttl", ttl);//这里一定是int类型参数。
        args.put("x-dead-letter-exchange", TOPIC_ORDER_EXCHANGE_DLX);
        args.put("x-dead-letter-routing-key", "");//待会试一下，先随便写一个
        return new Queue(ORDER_SEND_QUEUE, true, false, false, args);
    }

    //绑定
    @Bean
    public Binding binding4Order(@Qualifier(ORDER_SEND_QUEUE) Queue queue,
                                 @Qualifier(TOPIC_ORDER_EXCHANGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("#")
                .noargs();
    }

    public static final String TOPIC_ORDER_EXCHANGE_DLX = "topic_order_exchange_dlx";
    public static final String ORDER_SEND_QUEUE_DLX = "order_send_queue_dlx";

    /*死信队列*/
    @Bean(TOPIC_ORDER_EXCHANGE_DLX)
    public TopicExchange topic_order_Exchange_dlx() {
        return new TopicExchange(TOPIC_ORDER_EXCHANGE_DLX, true, false);
    }

    @Bean(ORDER_SEND_QUEUE_DLX)
    public Queue order_send_queue_dlx() {
        return new Queue(ORDER_SEND_QUEUE_DLX, true, false, false);
    }

    @Bean
    public Binding binding4Orderdlx(@Qualifier(ORDER_SEND_QUEUE_DLX) Queue queue,
                                    @Qualifier(TOPIC_ORDER_EXCHANGE_DLX) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("#")
                .noargs();
    }


}
