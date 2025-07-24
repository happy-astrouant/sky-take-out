package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.context.BaseContext;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
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

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private DishMapper dishMapper;



    @Override
    @Transactional(readOnly = true)
    public PageResult page(Integer page, Integer pageSize, String name, Integer type) {
        PageResult pageResult = new PageResult();
        PageHelper.startPage(page, pageSize);
        List<Category> categoryList = categoryMapper.page(name, type);
        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        pageResult.setRecords(pageInfo.getList());
        pageResult.setTotal(pageInfo.getTotal());
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
        Category category = new Category();
        category.setStatus(status);
        category.setId(id);
        categoryMapper.updateStatus(category);
    }

    @Override
    @Transactional
    public boolean save(Category category) {
        if(categoryMapper.countByName(category.getName()) > 0){
            return false;
        }
        categoryMapper.save(category);
        return true;
    }

    @Override
    public int delete(Long id) {
        // 先查询出该分类的类型
        Integer type = categoryMapper.getTypeById(id);
        // 如果分类是菜品，需要去菜品数据库中检查是否存在关联
        if(type == 1){
            if(dishMapper.countByCategoryId(id) > 0){
                return 1;
            }
        } else if(type == 2){
            if(setmealMapper.countByCategoryId(id) > 0){
                return 2;
            }
        }

        categoryMapper.delete(id);
        return 0;
    }

    @Override
    public List<Category> list(Integer type) {
        return categoryMapper.list(type);
    }

    @Override
    public List<Category> listAll() {
        return categoryMapper.listAll();
    }


}
