package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.sky.context.BaseContext;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Transactional
    public PageResult page(Integer page, Integer pageSize, String name, Integer type) {
        int num = categoryMapper.count(name, type);

        PageHelper.startPage(page, pageSize);
        PageResult pageResult = new PageResult();
        List<Category> categoryList = categoryMapper.page(name, type);
        pageResult.setTotal(num);
        pageResult.setRecords(categoryList);
        return pageResult;
    }

    @Override
    public void update(Category category) {
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.update(category);
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        categoryMapper.updateStatus(status, id, LocalDateTime.now(), BaseContext.getCurrentId());
    }


}
