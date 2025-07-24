package com.sky.controller.user;


import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Result<List<Setmeal>> list(Integer categoryId) {
        log.info("查询分类id为{}的套餐数据", categoryId);
        String key = "setmeal_" + categoryId;
        List<Setmeal> list = (List<Setmeal>) redisTemplate.opsForValue().get(key);
        if(list == null){
            list = setmealService.getSetmealsByCategoryId(categoryId);
            redisTemplate.opsForValue().set(key, list);
            log.info("缓存未命中，将数据缓存到Redis");
        }
        return Result.success(list);
    }

    @GetMapping("/dish/{id}")
    public Result<List<DishItemVO>> getByIdWithDish(Long id) {
        log.info("查询套餐id为{}的菜品数据集合", id);
        String key = "dishFlavor_" + id;
        List<DishItemVO> list = (List<DishItemVO>) redisTemplate.opsForValue().get(key);
        if(list == null){
            list = dishService.getDishItemBySetmealId(id);
            redisTemplate.opsForValue().set(key, list);
        }
        return Result.success(list);
    }

}
