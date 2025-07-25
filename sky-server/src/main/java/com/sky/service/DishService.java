package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    void update(DishDTO dishDTO);

    void delete(Long[] ids);

    void save(DishDTO dishDTO);

    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    DishVO getByIdWithFlavors(Long id);

    List<Dish> list(Integer categoryId);

    List<DishVO> listWithFlavor(Integer categoryId);

    void updateStatus(Integer status, Long id);

    List<DishItemVO> getDishItemBySetmealId(Long id);
}
