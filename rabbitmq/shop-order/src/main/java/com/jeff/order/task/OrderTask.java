package com.jeff.order.task;


import com.alibaba.fastjson.JSONObject;
import com.jeff.order.config.MQConfiguration;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@EnableScheduling
public class OrderTask {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Scheduled(cron = "0 */10 * * *") //每10分钟执行一次
    public void reSend() {
        // 查出需要重发的消息
        String qrySQL = "select * from t_order_message where sync_status!=-1 ";
        List<OrderMessage> resutl = jdbcTemplate.query(qrySQL, rs -> {
            List<OrderMessage> qryRS = new ArrayList<>();
            while (rs.next()) {
                OrderMessage orderMessage = new OrderMessage(rs.getInt(1), rs.getString(2)
                        , rs.getString(3), rs.getTimestamp(4));
                qryRS.add(orderMessage);
            }
            return qryRS;
        });
        for (OrderMessage orderMessage : resutl) {
            //执行重新发送
            String jsonBill = JSONObject.toJSONString(orderMessage);
            rabbitTemplate.convertAndSend(MQConfiguration.TOPIC_ORDER_EXCHANGE, "", jsonBill,
                    new CorrelationData(String.valueOf(orderMessage.getId())));
        }

        /*
        for (int i = 0; i <; i++) {
            rabbitTemplate.convertAndSend();

        }
        */
    }


}

class OrderMessage {
    private Integer id;
    private String order_content;
    private String send_content;
    private Timestamp ts;

    public OrderMessage(Integer id, String order_content, String send_content, Timestamp ts) {
        this.id = id;
        this.order_content = order_content;
        this.send_content = send_content;
        this.ts = ts;
    }

    public OrderMessage() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrder_content() {
        return order_content;
    }

    public void setOrder_content(String order_content) {
        this.order_content = order_content;
    }

    public String getSend_content() {
        return send_content;
    }

    public void setSend_content(String send_content) {
        this.send_content = send_content;
    }

    public Timestamp getTs() {
        return ts;
    }

    public void setTs(Timestamp ts) {
        this.ts = ts;
    }
}