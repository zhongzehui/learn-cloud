package com.jeff.send.mapper;

import com.jeff.send.vo.SendVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SendMapper {

    public Integer addSendVO(SendVO sendVO);

//    public AdInfo qryAdInfoById(Long id);
//
//    public List<AdInfo> qryAll();
//
//    public List<AdInfo> qryByCondition(String condition);
//
//    public AdInfo updateAdInfo(AdInfo adInfo);
//
//    public boolean delAdInfo(Long id);
}
