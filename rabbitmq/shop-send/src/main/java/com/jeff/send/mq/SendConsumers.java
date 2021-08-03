package com.jeff.send.mq;


import com.alibaba.fastjson.JSONObject;
import com.jeff.send.config.MQConfiguration;
import com.jeff.send.vo.OrderVO;
import com.jeff.send.service.SendService;
import com.jeff.send.vo.SendVO;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.UUID;

@Component
public class SendConsumers {

    @Autowired
    private SendService sendService;

    /*
     解决消息重试的集中方案：

     1：控制重发的次数+ 死信队列
     2：try/catch + 手动ack
     2：try/catch + 手动ack + 死信队列处理+ 人工干预


控制重发配置

     */

    @RabbitListener(id = "consumneOrder", queues = {MQConfiguration.ORDER_SEND_QUEUE})
    public void consumneOrder(String orderMsg, Channel channel, CorrelationData correlationData,
                              @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
        System.out.println("获取到的消息是===>" + orderMsg);
        Integer sendID = null;

        try {
            //消息转vo
            OrderVO orderVO = ((JSONObject) JSONObject.parse(orderMsg)).toJavaObject(OrderVO.class);
            //
            Integer orderID = orderVO.getId();
            String string = correlationData.getId();
            sendID = sendService.addSendVO(new SendVO(orderVO.getOrder_content(),
                    UUID.randomUUID().toString(), new Timestamp(System.currentTimeMillis())));
            if (sendID != null) {
                //执行确认机制
                channel.basicAck(tag, false);
            } else {
                //拒绝接受消息
                channel.basicNack(tag, false, false);
                //如果设置requeue为true重试，会重试几次，实在还是失败，就会吧消息丢弃
                //设置为false，直接丢弃，淦！
            }


        } catch (Exception e) {
            /*
             * try/catch会导致重复机制失效！！重试与catch异常是互斥的
             * */

            /* 如果出现异常的情况下 ，根据实际的的情况去进行重发
             * 重发一次之后、丢失、日志记录、存库；根据自己的业务场景去决定
             * 参数1：tag 消息的tag    参数2：multiple 多条处理 false   参数3： requeue重发
             * false 不会重发，会把消息保存到死信队列
             * true 会死循环重发，建议如果使用true，不加try/catch ，否则会造成死循环
             * */

            channel.basicNack(tag, false, false);
        }

    }
}
