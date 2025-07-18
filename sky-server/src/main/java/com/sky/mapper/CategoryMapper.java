package com.sky.mapper;


import com.sky.entity.Category;
import com.sky.result.PageResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import java.util.List;

@Mapper
public interface CategoryMapper {


    List<Category> page(String name, Integer type);

    Integer count(String name, Integer type);
}
