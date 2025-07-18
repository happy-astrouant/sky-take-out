package com.sky.service;


import com.sky.entity.Category;
import com.sky.result.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CategoryService {
    PageResult page(Integer page, Integer pageSize, String name, Integer type);

    void update(Category category);

    void startOrStop(Integer status, Long id);

    boolean save(Category category);

    int delete(Long id);

    List<Category> list(Integer type);
}
