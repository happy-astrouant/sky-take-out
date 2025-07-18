package com.sky.mapper;

import com.sky.dto.DishDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DishMapper {

    @Select(value = "select count(1) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    void update(DishDTO dishDTO);
}
