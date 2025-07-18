package com.sky.service;


import com.sky.result.PageResult;
import org.springframework.stereotype.Service;


public interface CategoryService {
    PageResult page(Integer page, Integer pageSize, String name, Integer type);
}
