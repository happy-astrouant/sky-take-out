package com.sky.controller.admin;


import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    /*修改菜品*/
    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO) {
        dishService.update(dishDTO);
        return Result.success();
    }

    /**
     * 批量删除菜品
     */
    @DeleteMapping
    public Result delete(Long[] ids) {
        // 需要检查订单中是否关联该菜品
        try{
            dishService.delete(ids);
        } catch (DeletionNotAllowedException e){
            return Result.error(e.getMessage());
        }
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

    /**
     * 根据分类ID查询菜品
     */
    @GetMapping("/list")
    public Result<List<Dish>> list(Integer categoryId) {
        // 仅需要查询菜品本身
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }

    /**
     * 起售/停售
     */
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable Integer status, Long id) {
        dishService.updateStatus(status, id);
        return Result.success();
    }

}
