package com.jeff.order.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MQSpecialConfig {

    //设置过期的队列
    @Bean("points_queue_ttl")
    public Queue points_queue_ttl(){
        //设置队列消息过期时间
        Map<String,Object> arge = new HashMap<>();
        int ttl = 20_000;
        arge.put("x-message-ttl",ttl);//这里一定是int类型参数。
        return new Queue("points_queue_ttl", true, false, false,arge);
    }

    //注册绑定信息
    @Bean
    public Binding points_queue_ttlBindding(@Qualifier("points_queue_ttl") Queue queue,
                             @Qualifier("topic_exchange") Exchange exchange){
        //试一下不用routingkey可不可以使用topic交换机
        return  BindingBuilder.bind(queue).to(exchange).with("")
                .noargs();
    }

    //配置死信队列


}
