package com.sky.controller.admin;


import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        int res = dishService.delete(ids);
        if(res == 1)
            return Result.error(MessageConstant.DISH_ON_SALE);
        if(res == 2)
            return Result.error(MessageConstant.SETMEAL_ON_SALE);
        return Result.success();
    }

    /**
     * 新增菜品
     */
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO) {
        dishService.save(dishDTO);
        return Result.success();
    }

    /**
     * 菜品分页查询
     */
    @GetMapping("/page")
    public Result page(DishPageQueryDTO dishPageQueryDTO) {
        PageResult pageResult = dishService.page(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据ID查询详细菜品
     */
    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id) {

        DishVO dishVO = dishService.getByIdWithFlavors(id);
        return Result.success(dishVO);
    }
}
