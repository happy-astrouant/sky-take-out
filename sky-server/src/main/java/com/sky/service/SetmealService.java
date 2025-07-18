package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;

public interface SetmealService {
    void update(SetmealDTO setmealDTO);

    PageResult page(SetmealPageQueryDTO query);

    void save(SetmealDTO setmealDTO);

    void updateStatus(Integer status, Long id);
}
