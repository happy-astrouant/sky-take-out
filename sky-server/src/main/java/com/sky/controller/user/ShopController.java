package com.sky.controller.user;


import com.sky.result.Result;
import com.sky.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "userShopController")
@RequestMapping("/user/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @GetMapping("/status")
    public Result getStatus(){
        return Result.success(shopService.getStatus());
    }
}
