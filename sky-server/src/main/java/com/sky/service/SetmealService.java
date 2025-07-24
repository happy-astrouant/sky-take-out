package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    void update(SetmealDTO setmealDTO);

    PageResult page(SetmealPageQueryDTO query);

    void save(SetmealDTO setmealDTO);

    void updateStatus(Integer status, Long id);

    void delete(List<Long> ids);

    SetmealVO getByIdWithDish(Long id);

    List<Setmeal> getSetmealsByCategoryId(Integer categoryId);
}
