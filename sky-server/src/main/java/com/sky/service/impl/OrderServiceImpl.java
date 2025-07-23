package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.MessageConstant;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.entity.Orders;
import com.sky.exception.BaseException;
import com.sky.mapper.OrderMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
        List<Map<String, Object>> maps =  orderMapper.statistics();
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        for(Map<String, Object>map: maps){
            switch (map.get("status").toString()){
                case "2":
                    orderStatisticsVO.setToBeConfirmed((Integer) map.getOrDefault("count", 0));
                    break;
                case "3":
                    orderStatisticsVO.setConfirmed((Integer) map.getOrDefault("count", 0));
                    break;
                case "4":
                    orderStatisticsVO.setDeliveryInProgress((Integer) map.getOrDefault("count", 0));
                    break;
            }
        }
        return orderStatisticsVO;
    }

    @Override
    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
        Orders order = new Orders();
        BeanUtils.copyProperties(ordersConfirmDTO, order);
        order.setStatus(OrderVO.CONFIRMED);
        orderMapper.updateOrder(order);
    }

    @Override
    public void delivery(Long id) {
        Orders order = new Orders();
        order.setId(id);
        order.setStatus(OrderVO.DELIVERY_IN_PROGRESS);
        orderMapper.updateOrder(order);
    }

    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        List<OrderVO> list = orderMapper.conditionSearch(dto);
        PageInfo<OrderVO> pageInfo = new PageInfo<>(list);
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }
}
