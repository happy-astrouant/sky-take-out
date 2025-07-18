package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
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

    @Autowired
    private OrderMapper orderMapper;

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

    @Override
    public int delete(Long[] ids) {
        // 已经起售的菜品禁止删除
        if(dishMapper.countEnableDishByIds(ids) > 0){
            return 1;
        }
        // 检查套餐中是否有该菜品
        if(dishMapper.countSetmealByDishIds(ids) > 0){
            return 2;
        }
        // 删除关联的口味数据
        dishFlavorMapper.deleteByDishIds(ids);
        dishMapper.deleteByIds(ids);
        return 0;
    }

    @Override
    public void save(DishDTO dishDTO) {
        // TODO: 文件上传
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.save(dish);
        // 新增口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dish.getId()));
        }
    }

    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishPageQueryDTO, dish);
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        List<Dish> page = dishMapper.page(dish);
        PageInfo<Dish> pageInfo = new PageInfo<>(page);
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }
}
