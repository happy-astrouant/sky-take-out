package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DishMapper {

    @Select(value = "select count(1) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    void deleteByIds(Long[] ids);

    @AutoFill(value = OperationType.INSERT)
    void save(Dish dish);


    int countEnableDishByIds(Long[] ids);

    int countSetmealByDishIds(Long[] ids);

    List<Dish> page(Dish dish);

    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    @Select("select * from dish where category_id = #{categoryId}")
    List<Dish> listByCategoryId(Integer categoryId);

    @AutoFill(value = OperationType.UPDATE)
    @Update("update dish set status = #{status} where id = #{id}")
    void updateStatus(Dish dish);

    @Update("update setmeal_dish set name = #{name}, price = #{price} where dish_id = #{id}")
    void updateSetmeal(Dish dish);
}
