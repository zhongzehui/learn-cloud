package com.jeff.order.controller;


import com.jeff.order.service.OrderService;
import com.jeff.order.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.sql.Timestamp;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("makeOrder")
    public String makeOrder() {
        orderService.addOrder(new OrderVO("买辣翅一对",
                new Timestamp(System.currentTimeMillis() ) ) );
        return "sucess";
    }

}
