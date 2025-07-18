package com.sky.controller.admin;


import com.sky.constant.MessageConstant;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/admin/category")
public class CategoryController {


    @Autowired
    private CategoryService categoryService;

    /**
     * 分类分页查询

     */
    @GetMapping("/page")
    public Result<PageResult> page(
            @RequestParam Integer page,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer type) {
        PageResult pageResult = categoryService.page(page, pageSize, name, type);
        return Result.success(pageResult);
    }


    /**
     * 修改分类
     */
    @PutMapping
    public Result update(@RequestBody Category category){
        categoryService.update(category);
        return Result.success();
    }

    /**
     * 启用、禁用分类
     */
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        log.info("分类{}，状态设置为：{}", id, status);
        categoryService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * 新增分类
     */
    @PostMapping
    public Result save(@RequestBody Category category) {
        log.info("新增分类：{}", category);
        boolean res = categoryService.save(category);
        if(res)
            return Result.success();
        else
            return Result.error(MessageConstant.CATEGORY_ALREADY_EXIST);
    }

    /**
     * 根据id删除分类
     */
    @DeleteMapping
    public Result delete(@RequestParam Long id) {
        log.info("删除分类：{}", id);
        int res = categoryService.delete(id);
        if(res == 1)
            return Result.error(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        if(res == 2)
            return Result.error(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        return Result.success();
    }

    /**
     * 按类型查询分类
     */
    @GetMapping("/list")
    public Result list(Integer type) {
        log.info("查询分类：{}", type);
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }
}
