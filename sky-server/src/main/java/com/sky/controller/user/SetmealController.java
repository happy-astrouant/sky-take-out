package com.sky.controller.user;


import com.sky.constant.CacheConstant;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userSetmealController")
@Slf4j
@RequestMapping("/user/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/list")
    @Cacheable(value = CacheConstant.CATEGORY_SETMEAL_CACHE, key = "#categoryId")
    public Result<List<Setmeal>> list(Integer categoryId) {
        log.info("查询分类id为{}的套餐数据", categoryId);
        List<Setmeal> list = setmealService.getSetmealsByCategoryId(categoryId);
        return Result.success(list);
    }

    @GetMapping("/dish/{id}")
    @Cacheable(value = CacheConstant.SETMEAL_DISH_CACHE, key = "#id")
    public Result<List<DishItemVO>> getByIdWithDish(Long id) {
        log.info("查询套餐id为{}的菜品数据集合", id);
        List<DishItemVO> list = dishService.getDishItemBySetmealId(id);
        return Result.success(list);
    }

}
