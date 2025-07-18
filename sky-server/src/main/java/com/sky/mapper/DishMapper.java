package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DishMapper {

    @Select(value = "select count(1) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @AutoFill(value = OperationType.UPDATE)
    void update(DishDTO dishDTO);

    void deleteByIds(Long[] ids);

    @AutoFill(value = OperationType.INSERT)
    void save(Dish dish);


    int countEnableDishByIds(Long[] ids);

    int countSetmealByDishIds(Long[] ids);
}
