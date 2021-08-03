package com.jeff.order.service;

import com.alibaba.fastjson.JSONObject;
import com.jeff.order.config.MQConfiguration;
import com.jeff.order.mapper.OrderMapper;
import com.jeff.order.vo.OrderVO;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //开启事务  //设置3s超时   这个超时是针对数据库操作的，艹
    @Transactional(rollbackFor = Exception.class,timeout = 3)
    public Integer addOrder(OrderVO orderVO) {
        Integer id;
        id = orderMapper.addOrder(orderVO);
        //执行推送派送信息
        //使用队列来保证业务最终一致性
        // 添加保存队列信息到数据库(消息冗余)
        Integer recordID = saveLocalMessage(orderVO);
        //发消息到队列
        sendMessage2MQ(orderVO);
        //分布式常规操作，通过接口调用处理
        /*
        HttpHeaders postHeaders = new HttpHeaders();
        postHeaders.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));
        HttpEntity<String> stringHttpEntity = new HttpEntity<>(JSONObject.toJSON(orderVO).toString(), postHeaders);
        String result = restTemplate.postForObject("http://localhost:8050/send/sendInfo?orderInfo=hahah", stringHttpEntity, String.class);
*/
        return id;
    }

    private void sendMessage2MQ(OrderVO orderVO) {
        String jsonBill = JSONObject.toJSONString(orderVO);
        rabbitTemplate.convertAndSend(MQConfiguration.TOPIC_ORDER_EXCHANGE, "", jsonBill,
                new CorrelationData(String.valueOf(orderVO.getId())));
    }

    private Integer saveLocalMessage(OrderVO orderVO) {

        Integer id;

        //使用jdbc插入备份消息 ，其实不应该在这里维护send——content的
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String insertSql = "insert into t_order_message (order_id," +
                "send_content," +
                "order_content," +
                "sync_status) " +

                "values(?,?,?,?);";
        /*String insertSql = "insert into t_order_message (order_id,send_content,order_content,sync_status) values(" +
                "'"+ orderVO.getId()+ "',"+ "'"+ UUID.randomUUID().toString() +"',"+ "'"+ orderVO.getOrder_content()+ "'　,"+ "　０"+
                ")";*/
        jdbcTemplate.update(connection -> {
            //如果需要返回GeneratedKeys，则PreparedStatement需要显示添加一个参数Statement.RETURN_GENERATED_KEYS。不过直接报错
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS );
            preparedStatement.setObject(1, orderVO.getId());
            preparedStatement.setString(2, UUID.randomUUID().toString());
            preparedStatement.setString(3, orderVO.getOrder_content());
            preparedStatement.setObject(4, 0);
            return preparedStatement;
        }, keyHolder);

        return keyHolder.getKey().intValue();


    }
}
