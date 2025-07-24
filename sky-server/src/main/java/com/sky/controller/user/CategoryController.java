package com.sky.controller.user;


import com.sky.constant.CacheConstant;
import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userCategoryController")
@Slf4j
@RequestMapping("/user/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    @Cacheable(value = CacheConstant.CATEGORY_SET_CACHE, key = "#type != null ? #type : 'all'")
    public Result<List<Category>> getCategoryList(@RequestParam(required = false) Integer type){
        return Result.success(categoryService.list(type));
    }
}
