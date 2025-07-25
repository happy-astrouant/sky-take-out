package com.sky.controller.user;


import com.sky.dto.ShoppingCartDTO;
import com.sky.result.Result;
import com.sky.service.ShoppingService;
import io.swagger.v3.oas.models.annotations.OpenAPI31;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/user/shoppingCart")
@Slf4j
public class ShoppingController {

    @Autowired
    private ShoppingService shoppingService;

    @RequestMapping("/add")
    public Result add(@RequestBody ShoppingCartDTO dto) {
        shoppingService.add(dto);
        return Result.success();
    }
}
