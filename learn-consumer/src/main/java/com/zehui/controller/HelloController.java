package com.zehui.controller;


import com.zehui.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @Autowired
    private IUserService userService;

    @GetMapping("hello")
    @ResponseBody
    public String hello(){
        userService.buyGood(" googs");
        return "success";
    }
}
