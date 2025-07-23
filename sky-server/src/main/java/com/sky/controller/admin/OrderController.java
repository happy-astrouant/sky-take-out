package com.sky.controller.admin;


import com.sky.dto.*;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/admin/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**查询订单详情*/
    @GetMapping("details/{id}")
    public Result details(Long id) {
        OrderVO orderVO = orderService.getById(id);
        return Result.success(orderVO);
    }

    /*取消订单*/
    @PutMapping("/cancel")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        orderService.cancel(ordersCancelDTO);
        return Result.success();
    }

    /*拒绝订单*/
    @PutMapping("/rejection")
    public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
        orderService.rejection(ordersRejectionDTO);
        return Result.success();
    }

    /**接单*/
    @PutMapping("/confirm")
    public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        orderService.confirm(ordersConfirmDTO);
        return Result.success();
    }

    /**派送订单*/
    @PutMapping("/delivery/{id}")
    public Result delivery(@PathVariable Long id) {
        orderService.delivery(id);
        return Result.success();
    }

    /**完成订单*/
    @PutMapping("/complete/{id}")
    public Result complete(@PathVariable Long id) {
        orderService.complete(id);
        return Result.success();
    }

    /*订单统计接口*/
    @GetMapping("/statistics")
    public Result statistics() {
        return Result.success(orderService.statistics());
    }

    @GetMapping("/conditionSearch")
    public Result conditionSearch(
            @RequestParam(required = false) LocalDateTime beginTime,
            @RequestParam(required = false) LocalDateTime endTime,
            @RequestParam(required = false) Integer status,
            @RequestParam Integer page,
            @RequestParam Integer pageSize) {

        OrdersPageQueryDTO dto = new OrdersPageQueryDTO();
        dto.setBeginTime(beginTime);
        dto.setEndTime(endTime);
        dto.setStatus(status);
        dto.setPage(page);
        dto.setPageSize(pageSize);
        return Result.success(orderService.conditionSearch(dto));
    }

}
