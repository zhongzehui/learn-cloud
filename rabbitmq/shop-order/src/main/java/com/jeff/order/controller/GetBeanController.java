package com.jeff.order.controller;


import com.jeff.order.OrderApplication;
import javafx.application.Application;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*使用applicationcontext获取bean*/
@RestController
public class GetBeanController {


    @RequestMapping("getBean/{name}")
    public String getBean(@PathVariable("name") String beanName){

        JdbcTemplate jdbcTemplate = OrderApplication.applicationContext.getBean(JdbcTemplate.class);
        OrderApplication.applicationContext.getBean("name");
        return "success";

    }
}
