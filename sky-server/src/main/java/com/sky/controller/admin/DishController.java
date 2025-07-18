package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.result.Result;
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
    public Result update(DishDTO dishDTO) {
        dishService.update(dishDTO);
        return Result.success();
    }

    /**
     * 批量删除菜品
     */
    public Result delete(Long[] ids) {
        // 需要检查订单中是否关联该菜品
        boolean res = dishService.delete(ids);
        if(!res)
            return Result.error("删除菜品失败，订单中存在该菜品");
        return Result.success();
    }
}
