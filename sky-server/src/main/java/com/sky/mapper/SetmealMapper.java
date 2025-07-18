package com.sky.mapper;


import com.sky.annotation.AutoFill;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    void update(Setmeal setmeal);

    @AutoFill(value = OperationType.UPDATE)
    @Select("select count(1) from setmeal where category_id = #{categoryId}")
    int countByCategoryId(Long categoryId);

    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteBySetmealId(Long seatmealId);


    void insertSetmealDish(List<SetmealDish> setmealDishes);
}
