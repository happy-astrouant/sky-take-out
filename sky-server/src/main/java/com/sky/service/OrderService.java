package com.sky.service;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.Result;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;

public interface OrderService {
    OrderVO getById(Long id);

    void cancel(OrdersCancelDTO ordersCancelDTO);

    void rejection(OrdersRejectionDTO ordersRejectionDTO);

    void complete(Long id);

    OrderStatisticsVO statistics();
}
