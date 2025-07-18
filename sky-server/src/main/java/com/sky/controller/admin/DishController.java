package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    /*修改菜品*/
    @PutMapping
    public void update(DishDTO dishDTO) {
        dishService.update(dishDTO);
    }
}
