package com.sky.service;


import com.sky.entity.Category;
import com.sky.result.PageResult;
import org.springframework.stereotype.Service;


public interface CategoryService {
    PageResult page(Integer page, Integer pageSize, String name, Integer type);

    void update(Category category);

    void startOrStop(Integer status, Long id);

    void save(Category category);
}
