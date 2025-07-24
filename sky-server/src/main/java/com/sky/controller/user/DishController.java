package com.sky.controller.user;

import com.sky.constant.CacheConstant;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 查询某个分类下的菜品列表
    @GetMapping("/list")
    @Cacheable(value = CacheConstant.CATEGORY_DISH_CACHE, key = "#categoryId")
    public Result<List<DishVO>> getDishList(@RequestParam Integer categoryId){
        List<DishVO> list = dishService.listWithFlavor(categoryId);
        return Result.success(list);
    }
}
