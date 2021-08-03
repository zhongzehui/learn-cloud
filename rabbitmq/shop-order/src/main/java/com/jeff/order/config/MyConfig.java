package com.jeff.order.config;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Configuration
public class MyConfig {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;
    //确认机制保证消息的可靠生产
    @PostConstruct
    public void regCallbac(){

        rabbitTemplate.setConfirmCallback((correlationData,ack,cause)->{
            System.out.println("cause："+cause);
            String orderID = correlationData.getId();
            if(!ack) {
                //记录日志
//                邮件通知
                System.out.println("MQ队列应答失败，orderID是："+ orderID);
                return;
            }
            //更新消息记录表
            String updateSQL = "update t_order_message set sync_status = -1 where order_ID='"+orderID +"' ";
            try {
                int count  = jdbcTemplate.update(updateSQL);
                if(count>0){
                    System.out.println("本地消息状态修改成功，消息成功投递到消息队列中");
                }
            } catch (DataAccessException e) {
//                记录日志、发送邮件等等
                System.out.println("更新本地消息失败："+ e.getMessage());
            }

        });
        /*rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {

            }
        });*/


    }

}
