package com.sky.controller.user;


import com.sky.context.BaseContext;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userOrderController")
@Slf4j
@RequestMapping("/user/order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    // 用户下单
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单：{}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submit(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    // 查询订单详情
    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> getById(@PathVariable Long id) {
        log.info("查询订单详情：{}", id);
        OrderVO orderVO = orderService.getById(id);
        return Result.success(orderVO);
    }

    // 查询历史订单，需要展示每个订单的菜品细节
    @GetMapping("/historyOrders")
    public Result<PageResult> historyOrders(Integer page, Integer pageSize, @RequestParam(required = false) Integer status) {
        Long userId = BaseContext.getCurrentId();
        log.info("User {} 查询历史订单", userId);
        OrdersPageQueryDTO dto = new OrdersPageQueryDTO();
        dto.setPage(page);
        dto.setPageSize(pageSize);
        dto.setStatus(status);
        dto.setUserId(BaseContext.getCurrentId());
        PageResult pageResult = orderService.userHistoryOrders(dto);
        return Result.success(pageResult);
    }

    // 再来一单
    @PostMapping("/repetition/{id}")
    public Result repetition(@PathVariable Long id) {
        log.info("再来一单：{}", id);
        orderService.repetition(id);
        return Result.success();
    }

    // 取消订单
    @PutMapping("/cancel/{id}")
    public Result cancel(@PathVariable Long id) {
        log.info("User {} 取消订单：{}",BaseContext.getCurrentId(), id);
        OrdersCancelDTO ordersCancelDTO = new OrdersCancelDTO();
        ordersCancelDTO.setId(id);
        ordersCancelDTO.setCancelReason("用户取消");
        orderService.cancel(ordersCancelDTO);
        return Result.success();
    }

    // 订单支付
    @PutMapping("/payment")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO dto) {
        log.info("订单支付：{}", dto);
        OrderPaymentVO vo = orderService.payment(dto);
        return Result.success(vo);
    }

    // 催单
    @GetMapping("/reminder/{id}")
    public Result reminder(@PathVariable Long id) {
        log.info("User {} 催单：{}", BaseContext.getCurrentId(), id);
        return Result.success();
    }

}
