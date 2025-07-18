package com.sky.service;

import com.sky.dto.DishDTO;

public interface DishService {
    void update(DishDTO dishDTO);

    int delete(Long[] ids);

    void save(DishDTO dishDTO);
}
