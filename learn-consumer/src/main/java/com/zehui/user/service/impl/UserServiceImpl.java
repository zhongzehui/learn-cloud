package com.zehui.user.service.impl;

import com.zehiu.order.IOrderService;
import com.zehui.user.IUserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements IUserService {

    @DubboReference
    private IOrderService orderService;

    @Override
    public String buyGood(String param) {

        System.out.println("开始获取dubbo调用结果");
        System.out.println("结果是：" + orderService.makeBill("from userService .."));

        return "hahah";
    }
}
