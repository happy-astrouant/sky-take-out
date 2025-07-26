package com.sky.task;


import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    @Scheduled(cron = "0 0/5 * * * ?")
    public void autoCancelOrder(){
        log.info("开始执行自动取消订单任务");
        // 查询订单状态为待付款的订单
        List<Orders> orderList =
                orderMapper.getByStatusAndOrderTimeLT(
                        Orders.PENDING_PAYMENT,
                        LocalDateTime.now().plusMinutes(-15));
        // 设置状态为已取消
        if (orderList != null) {
            for(Orders orders : orderList){
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单长时间未支付，自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.updateOrder(orders);
            }
        }
    }

    // 每天凌晨2点检查一次
    @Scheduled(cron = "0 0 2 1/1 * ?")
    public void autoCompleteOrder(){
        log.info("开始执行自动确认收货任务");
        // 获取所有状态为派送中的订单
        List<Orders> orderList = orderMapper.getByStatusAndOrderTimeLT(
                Orders.DELIVERY_IN_PROGRESS,
                LocalDateTime.now().plusDays(-1));
        if(orderList != null){
            for(Orders orders: orderList){
                orders.setStatus(Orders.COMPLETED);
                orderMapper.updateOrder(orders);
            }
        }

    }
}
