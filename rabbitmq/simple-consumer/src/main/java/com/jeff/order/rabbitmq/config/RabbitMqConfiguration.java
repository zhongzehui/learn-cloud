package com.jeff.order.rabbitmq.config;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfiguration {


    public static  final String QUEUE_ORDER = "#.order.#";
    public static  final String QUEUE_CART = "#.CART.#";
    //注册交换机
    @Bean("fanout_Exchange")
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("fanout_Exchange",true,false);
    }
    @Bean("topic_Exchange")
    public TopicExchange topicExchange(){
        return  new TopicExchange("topic_Exchange",true,false);
    }
    @Bean("direct_Exchange")
    public DirectExchange directExchange(){
        return new DirectExchange("direct_Exchange",true,false);
    }

    //注册队列
    @Bean("message_queue")
    public Queue message_queue(){
        return new Queue("message_queue", true, false, false);
    }
    @Bean("email_queue")
    public Queue email_queue(){
        return new Queue("email_queue", true, false, false);
    }
    @Bean("wechat_queue")
    public Queue wechat_queue(){
        return new Queue("wechat_queue", true, false, false);
    }
    @Bean("points_queue")
    public Queue points_queue(){
        return new Queue("points_queue", true, false, false);
    }

    //注册绑定信息
    //订单
    @Bean
    public Binding binding01(@Qualifier("message_queue") Queue queue,
                             @Qualifier("topic_Exchange") Exchange exchange){
        //短信队列绑定订单topic的交换机
        return  BindingBuilder.bind(queue).to(exchange).with(QUEUE_ORDER)
                .noargs();
    }
    @Bean
    public Binding binding02(@Qualifier("email_queue") Queue queue,
                             @Qualifier("topic_Exchange") Exchange exchange){
        //邮件队列绑定订单topic的交换机
        return  BindingBuilder.bind(queue).to(exchange).with(QUEUE_ORDER)
                .noargs();
    }
    @Bean
    public Binding binding03(@Qualifier("wechat_queue") Queue queue,
                             @Qualifier("topic_Exchange") Exchange exchange){
        //微信通知队列绑定订单topic的交换机
        return  BindingBuilder.bind(queue).to(exchange).with(QUEUE_ORDER)
                .noargs();
    }
    @Bean
    public Binding binding04(@Qualifier("points_queue") Queue queue,
                             @Qualifier("topic_Exchange") Exchange exchange){
        //会员积分队列绑定订单topic的交换机
        return  BindingBuilder.bind(queue).to(exchange).with(QUEUE_ORDER)
                .noargs();
    }

    //针对购物车，只推送到微信和短信
    @Bean
    public Binding binding05(@Qualifier("wechat_queue") Queue queue,
                             @Qualifier("fanout_Exchange") Exchange exchange){
        //会员积分队列绑定订单topic的交换机
        return  BindingBuilder.bind(queue).to(exchange).with("")
                .noargs();
    }
    @Bean
    public Binding binding06(@Qualifier("message_queue") Queue queue,
                             @Qualifier("fanout_Exchange") Exchange exchange){
        //会员积分队列绑定订单topic的交换机
        return  BindingBuilder.bind(queue).to(exchange).with("")
                .noargs();
    }


    //注册 针对注解配置消费者 的交换机
    @Bean("annoutation_topic_Exchange")
    public TopicExchange annoutation_topic_Exchange(){
        return  new TopicExchange("annoutation_topic_Exchange",true,false);
    }
    //注册队列
    @Bean("annotation_message_queue")
    public Queue annotation_message_queue(){
        return new Queue("annotation_message_queue", true, false, false);
    }

    @Bean("annotation_email_queue")
    public Queue annotation_email_queue(){
        return new Queue("annotation_email_queue", true, false, false);
    }



}
