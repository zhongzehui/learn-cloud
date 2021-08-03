package com.zehui.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Map;

@Controller
public class HelloController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String,Object> redisTemplatel;

    @GetMapping("hello")
    @ResponseBody()
    public String hello(Map param, @RequestParam("id") String id){
        Object name  = //redisTemplatel.opsForValue().get("name").toString();
                redisTemplatel.opsForValue().get("name");

       //redisTemplatel.opsForValue().getOperations().delete("name");
        if(name!=null){
            System.out.println(name);
        }else{
            redisTemplatel.opsForValue().set("name", "钟泽辉");
        }


        return "success";
    }
}
