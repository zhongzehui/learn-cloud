package com.zehui.order.service.impl;

import com.zehiu.order.IOrderService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@DubboService
public class OrderServiceImpl  implements IOrderService {

    @Override
    public String makeBill(String param) {
        System.out.println("生成订单成功 :"+ param);
        return "生成订单成功, 时间是： "+ new Date().toString();
    }
}
