package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.mapper.OrderMapper;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public OrderVO getById(Long id) {
        OrderVO orderVO = orderMapper.getById(id);
        if(orderVO == null){
            throw new BaseException(MessageConstant.ORDER_NOT_FOUND);
        }
        return orderVO;
    }
}
