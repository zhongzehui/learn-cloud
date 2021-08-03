package com.jeff.send.service;

import com.jeff.send.mapper.SendMapper;
import com.jeff.send.vo.SendVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendService {

    @Autowired
    private SendMapper sendMapper;
    public Integer addSendVO(SendVO sendVO) {

        Integer id;
        id = sendMapper.addSendVO(sendVO);
        return id;
    }

}
