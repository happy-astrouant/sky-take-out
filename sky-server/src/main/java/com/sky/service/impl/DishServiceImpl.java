package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Override
    @Transactional
    public void update(DishDTO dishDTO) {
        // 需要同步更新dish_flavor表
        dishMapper.update(dishDTO);

        // 先删除旧数据
        dishFlavorMapper.deleteByDishId(dishDTO.getId());

        // 写入新数据
        List<DishFlavor> list = dishDTO.getFlavors();
        if(list != null && !list.isEmpty())
            dishFlavorMapper.batchInsert(dishDTO.getFlavors());
    }
}
