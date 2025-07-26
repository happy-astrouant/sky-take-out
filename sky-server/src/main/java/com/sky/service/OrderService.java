package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

import java.util.List;

public interface OrderService {
    OrderVO getById(Long id);

    void cancel(OrdersCancelDTO ordersCancelDTO);

    void rejection(OrdersRejectionDTO ordersRejectionDTO);

    void complete(Long id);

    OrderStatisticsVO statistics();

    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    void delivery(Long id);

    PageResult conditionSearch(OrdersPageQueryDTO dto);

    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

    PageResult userHistoryOrders(OrdersPageQueryDTO dto);

    void repetition(Long id);

    OrderPaymentVO payment(OrdersPaymentDTO dto);

    void reminder(Long id);
}
