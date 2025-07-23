package com.sky.controller.admin;


import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.Result;
import com.sky.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController(value = "adminShopController")
@RequestMapping("/admin/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @GetMapping("/status")
    public Result getStatus(){
        return Result.success(shopService.getStatus());
    }

    @PutMapping("/{status}")
    public Result changeStatus(@PathVariable Integer status){
        shopService.changeStatus(status);
        return Result.success();
    }
}
