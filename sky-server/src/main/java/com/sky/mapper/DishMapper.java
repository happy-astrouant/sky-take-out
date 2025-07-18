package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {

    @Select(value = "select count(1) from dish where category_id = #{categoryId}")
    public Integer countByCategoryId(Long categoryId);
}
