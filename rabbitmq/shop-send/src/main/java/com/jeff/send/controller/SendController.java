package com.jeff.send.controller;


import com.alibaba.fastjson.JSONObject;
import com.jeff.send.service.SendService;
import com.jeff.send.vo.SendVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class SendController {

    @Autowired
    private SendService sendService;

    @RequestMapping(value = "sendInfo", method = RequestMethod.POST)
    public String sendInfo(@RequestBody Map<String, Object> params, @RequestParam String orderInfo) {

        System.out.println("com.jeff.send.controller.SendController.sendInfo ===> 执行成功 ");
        if(true){
           // throw new RuntimeException("测试失败");
        }
       /* SendVO sendVO = new SendVO(params.get("order_content").toString(),
                params.get("send_content").toString(), new Timestamp( Long.parseLong(params.get("ts"))));*/

        JSONObject jsonObject = new JSONObject(params);
        SendVO sendVO = jsonObject.toJavaObject(SendVO.class);
        sendVO.setSend_content("小李去配送");

        sendService.addSendVO(sendVO);

        return "success";
    }
}
