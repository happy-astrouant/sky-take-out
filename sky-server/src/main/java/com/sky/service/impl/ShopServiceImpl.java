package com.sky.service.impl;

import com.sky.exception.BaseException;
import com.sky.mapper.ShopMapper;
import com.sky.result.Result;
import com.sky.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


@Service
public class ShopServiceImpl implements ShopService {

    private static final String SHOP_STATUS = "shop_status";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ShopMapper shopMapper;


    @Override
    public void changeStatus(Integer status) {
        stringRedisTemplate.opsForValue().set(SHOP_STATUS, status.toString());
        // 更新数据库内容
        shopMapper.updateStatus(status);
    }

    @Override
    public Integer getStatus() {
        String status = stringRedisTemplate.opsForValue().get(SHOP_STATUS);
        if (status != null) {
            return Integer.valueOf(status);
        } else {
            // 如果不存在，需要查询数据库中的店铺状态，然后设置缓存
            Integer real_status = shopMapper.getStatus();
            stringRedisTemplate.opsForValue().set(SHOP_STATUS, real_status.toString());
            return real_status;
        }
    }
}
