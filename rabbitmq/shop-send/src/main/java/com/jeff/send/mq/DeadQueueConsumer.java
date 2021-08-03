package com.jeff.send.mq;


import com.alibaba.fastjson.JSONObject;
import com.jeff.send.config.MQConfiguration;
import com.jeff.send.service.SendService;
import com.jeff.send.vo.SendVO;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DeadQueueConsumer {

    @Autowired
    private SendService sendService;

    @Transactional(rollbackFor = Exception.class)
    @RabbitListener(id = "deadQueueConsumer", queues = {MQConfiguration.ORDER_SEND_QUEUE_DLX})
    public void consumneOrder(String orderMsg, Channel channel, CorrelationData correlationData,
                              @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
        try {
            System.out.println("收到死信队列信息："+ orderMsg );
            //保存报错信息
            //处理死信队列消息
//            执行保存订单
//            注意幂等性问题
            JSONObject jsonObject = JSONObject.parseObject(orderMsg);
            SendVO sendVO = jsonObject.toJavaObject(SendVO.class);
            Integer id = sendService.addSendVO(sendVO);
            //执行确认
            if(id!=null){
                System.out.println("保存了配送信息：id="+id);
                channel.basicAck(tag, false);
            }

        }catch (Exception e){
            System.out.println("人工干预");
            System.out.println("发送短信预警");
            System.out.println("同时把消息转移别的存储DB");
            channel.basicNack(tag, false, false);
        }
    }
}
