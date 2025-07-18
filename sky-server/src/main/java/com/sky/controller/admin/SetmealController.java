package com.sky.controller.admin;


import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Result update(SetmealDTO setmealDTO) {
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
    public Result delete(@RequestBody List<Long> ids) {
        log.info("批量删除套餐：{}", ids);
        try {
            setmealService.delete(ids);
        } catch (SetmealEnableFailedException e){
            return Result.error(e.getMessage());
        }
        return Result.success();
    }

}
