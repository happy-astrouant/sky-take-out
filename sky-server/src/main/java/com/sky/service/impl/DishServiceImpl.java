package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
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
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);

        // 先删除旧数据
        dishFlavorMapper.deleteByDishId(dishDTO.getId());

        // 写入新数据
        List<DishFlavor> list = dishDTO.getFlavors();
        if(list != null && !list.isEmpty()){
            list.forEach(dishFlavor -> dishFlavor.setDishId(dishDTO.getId()));
            dishFlavorMapper.batchInsert(dishDTO.getFlavors());
        }

        // 还需同步更新套餐数据
        dishMapper.updateSetmeal(dish);
    }

    @Override
    public void delete(Long[] ids) {
        // 已经起售的菜品禁止删除
        if(dishMapper.countEnableDishByIds(ids) > 0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }
        // 检查套餐中是否有该菜品
        if(dishMapper.countSetmealByDishIds(ids) > 0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        // 删除关联的口味数据
        dishFlavorMapper.deleteByDishIds(ids);
        dishMapper.deleteByIds(ids);
    }

    @Override
    public void save(DishDTO dishDTO) {

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.save(dish);
        // 新增口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dish.getId()));
            dishFlavorMapper.batchInsert(flavors);
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

    @Override
    public DishVO getByIdWithFlavors(Long id) {
        // 首先查询dish表
        Dish dish = dishMapper.getById(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        // 查询口味数据
        List<DishFlavor> flavors = dishFlavorMapper.list(id);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    @Override
    public List<Dish> list(Integer categoryId) {
        return dishMapper.listByCategoryId(categoryId);
    }

    @Override
    public List<DishVO> listWithFlavor(Integer categoryId){
        List<DishVO> dishList = dishMapper.listDetailsByCategoryId(categoryId);
        for(DishVO dish: dishList){
            dish.setFlavors(dishFlavorMapper.list(dish.getId()));
        }
        return dishList;
    }

    @Override
    public void updateStatus(Integer status, Long id) {
        Dish dish = new Dish();
        dish.setStatus(status);
        dish.setId(id);
        dishMapper.updateStatus(dish);
    }
}
