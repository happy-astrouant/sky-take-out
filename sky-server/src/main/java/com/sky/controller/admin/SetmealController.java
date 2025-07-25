package com.sky.controller.admin;


import com.sky.constant.CacheConstant;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("admin/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /*
    * 修改套餐信息
    * */
    @PutMapping
    @Caching(evict = {
        @CacheEvict(value = CacheConstant.CATEGORY_SETMEAL_CACHE, key = "#setmealDTO.categoryId"),
        @CacheEvict(value = CacheConstant.SETMEAL_DISH_CACHE, key = "#setmealDTO.id")
    })
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        log.info("套餐-更新内容：{}", setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();
    }

    /*分页查询*/
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO query) {
        log.info("套餐-分页查询：{}", query);
        PageResult pageResult = setmealService.page(query);
        return Result.success(pageResult);
    }

    /**新增套餐*/
    @PostMapping
    @CacheEvict(value = CacheConstant.CATEGORY_SETMEAL_CACHE, key = "#setmealDTO.categoryId")
    public Result save(@RequestBody SetmealDTO setmealDTO) {
        log.info("套餐-保存：{}", setmealDTO);
        try {
            setmealService.save(setmealDTO);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        return Result.success();
    }

    /*开售或停售套餐*/
    @PostMapping("/status/{status}")
    @Caching(evict = {
        @CacheEvict(value = CacheConstant.CATEGORY_SETMEAL_CACHE, allEntries = true),
        @CacheEvict(value = CacheConstant.SETMEAL_DISH_CACHE, key = "#id")
    })
    public Result updateStatus(@PathVariable Integer status, Long id) {
        log.info("套餐-{} 起售或停售：{}", id, status);
        try{
            setmealService.updateStatus(status, id);
        } catch(SetmealEnableFailedException e){
            return Result.error(e.getMessage());
        }
        return Result.success();
    }

    /**批量删除套餐*/
    @DeleteMapping
    @Caching(evict = {
        @CacheEvict(value = CacheConstant.CATEGORY_SETMEAL_CACHE, allEntries = true),
        @CacheEvict(value = CacheConstant.SETMEAL_DISH_CACHE, allEntries = true)
    })
    public Result delete(@RequestParam List<Long> ids) {
        log.info("批量删除套餐：{}", ids);
        try {
            setmealService.delete(ids);
        } catch (SetmealEnableFailedException e){
            return Result.error(e.getMessage());
        }
        return Result.success();
    }

    /**根据ID查询套餐详细信息（含菜品）*/
    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        log.info("根据ID查询套餐详细信息：{}", id);
        SetmealVO setmealVO = setmealService.getByIdWithDish(id);
        return Result.success(setmealVO);
    }

}
