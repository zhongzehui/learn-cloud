package com.jeff.order.mapper;


import com.jeff.order.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface OrderMapper {
    public Integer addOrder(OrderVO orderVO);
}
