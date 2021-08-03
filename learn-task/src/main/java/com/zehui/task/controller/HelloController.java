package com.zehui.task.controller;


import com.zehui.task.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @Autowired
    HelloService helloService;

    @GetMapping("hello")
    @ResponseBody
    public String hello(){
        String helloAsync = helloService.helloAsync();
        //开启异步，不会返回内容 controller输出： null
        System.out.println("controller输出： " + helloAsync);
        return "Done";
    }

    @GetMapping("sendMail")
    @ResponseBody
    public String sendMail(){
        return  helloService.sendMail()? "发送成功":"发送失败";
    }

}
