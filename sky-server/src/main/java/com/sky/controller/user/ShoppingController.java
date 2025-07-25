package com.sky.controller.user;


import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingService;
import io.swagger.v3.oas.models.annotations.OpenAPI31;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/user/shoppingCart")
@Slf4j
public class ShoppingController {

    @Autowired
    private ShoppingService shoppingService;

    // 向购物车中添加菜品或套餐
    @RequestMapping("/add")
    public Result add(@RequestBody ShoppingCartDTO dto) {
        shoppingService.add(dto);
        return Result.success();
    }

    // 清空购物车
    @DeleteMapping("/clean")
    public Result clean() {
        shoppingService.clean();
        return Result.success();
    }

    // 查看购物车
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(){
        return Result.success(shoppingService.list());
    }

    // 删除一个购物车中的上坪
    @PostMapping("/sub")
    public Result sub(@RequestBody ShoppingCartDTO dto){
        shoppingService.sub(dto);
        return Result.success();
    }
}
