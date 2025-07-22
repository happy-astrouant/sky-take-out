package com.sky.service.impl;

import com.sky.exception.BaseException;
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


    @Override
    public void changeStatus(Integer status) {
        stringRedisTemplate.opsForValue().set(SHOP_STATUS, status.toString());
    }

    @Override
    public Integer getStatus() {
        String status = stringRedisTemplate.opsForValue().get(SHOP_STATUS);
        if (status != null) {
            return Integer.valueOf(status);
        } else {
            throw new BaseException("店铺状态异常");
        }
    }
}
