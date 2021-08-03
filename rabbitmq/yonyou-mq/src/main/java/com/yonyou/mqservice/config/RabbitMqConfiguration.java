package com.yonyou.mqservice.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 应该如何去管理这些配置呢？
 *      先定义一些默认的配置，
 *              拓展的配置，就手工完成
 */
@Configuration
public class RabbitMqConfiguration {



    public static final String QUEUE_ORDER = "#.order.#";

    public static final String QUEUE_CART = "#.CART.#";

    //注册交换机
    @Bean("fanout_exchange")
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanout_exchange", true, false);
    }

    @Bean("topic_exchange")
    public TopicExchange topicExchange() {
        return new TopicExchange("topic_exchange", true, false);
    }

    @Bean("direct_exchange")
    public DirectExchange directExchange() {
        return new DirectExchange("direct_exchange", true, false);
    }

    //注册队列
    @Bean("message_queue")
    public Queue message_queue() {
        return new Queue("message_queue", true, false, false);
    }

    @Bean("email_queue")
    public Queue email_queue() {

        return new Queue("email_queue", true, false, false);
    }

    @Bean("wechat_queue")
    public Queue wechat_queue() {
        return new Queue("wechat_queue", true, false, false);
    }

    @Bean("points_queue")
    public Queue points_queue() {
        return new Queue("points_queue", true, false, false);
    }

    //注册绑定信息
    //订单
    @Bean
    public Binding binding01(@Qualifier("message_queue") Queue queue,
                             @Qualifier("topic_exchange") Exchange exchange) {
        //短信队列绑定订单topic的交换机
        return BindingBuilder.bind(queue).to(exchange).with(QUEUE_ORDER)
                .noargs();
    }

    @Bean
    public Binding binding02(@Qualifier("email_queue") Queue queue,
                             @Qualifier("topic_exchange") Exchange exchange) {
        //邮件队列绑定订单topic的交换机
        return BindingBuilder.bind(queue).to(exchange).with(QUEUE_ORDER)
                .noargs();
    }

    @Bean
    public Binding binding03(@Qualifier("wechat_queue") Queue queue,
                             @Qualifier("topic_exchange") Exchange exchange) {
        //微信通知队列绑定订单topic的交换机
        return BindingBuilder.bind(queue).to(exchange).with(QUEUE_ORDER)
                .noargs();
    }

    @Bean
    public Binding binding04(@Qualifier("points_queue") Queue queue,
                             @Qualifier("topic_exchange") Exchange exchange) {
        //会员积分队列绑定订单topic的交换机
        return BindingBuilder.bind(queue).to(exchange).with(QUEUE_ORDER)
                .noargs();
    }

    //针对购物车，只推送到微信和短信
    @Bean
    public Binding binding05(@Qualifier("wechat_queue") Queue queue,
                             @Qualifier("fanout_exchange") Exchange exchange) {
        //会员积分队列绑定订单topic的交换机
        return BindingBuilder.bind(queue).to(exchange).with("")
                .noargs();
    }


    @Bean
    public Binding binding06(@Qualifier("message_queue") Queue queue,
                             @Qualifier("fanout_exchange") Exchange exchange) {
        //会员积分队列绑定订单topic的交换机
        return BindingBuilder.bind(queue).to(exchange).with("")
                .noargs();
    }


}
