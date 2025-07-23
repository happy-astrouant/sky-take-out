package com.sky.controller.admin;


import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersDTO;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Result rejection(@RequestBody OrderVO orderVO) {
        orderService.rejection(orderVO);
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

}
