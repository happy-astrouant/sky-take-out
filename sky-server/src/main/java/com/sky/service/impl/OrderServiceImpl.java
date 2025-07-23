package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.entity.Orders;
import com.sky.exception.BaseException;
import com.sky.mapper.OrderMapper;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    @Override
    public void cancel(OrdersCancelDTO ordersCancelDTO) {
        Orders order = new Orders();
        BeanUtils.copyProperties(ordersCancelDTO, order);
        order.setStatus(OrderVO.CANCELLED);
        order.setCancelTime(LocalDateTime.now());
        orderMapper.updateOrder(order);
    }

    @Override
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) {
        Orders order = new Orders();
        BeanUtils.copyProperties(ordersRejectionDTO, order);
         order.setStatus(OrderVO.CANCELLED);
         order.setCancelTime(LocalDateTime.now());
         orderMapper.updateOrder(order);
    }

    @Override
    public void complete(Long id) {
        Orders order = new Orders();
        order.setId(id);
        order.setStatus(OrderVO.COMPLETED);
        orderMapper.updateOrder(order);
    }

    @Override
    public OrderStatisticsVO statistics() {
        return orderMapper.statistics();
    }
}
