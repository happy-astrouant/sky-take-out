package com.sky.mapper;


import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.*;

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


    List<Setmeal> page(SetmealPageQueryDTO query);

    @AutoFill(value = OperationType.INSERT)
    void insert(Setmeal setmeal);

    @AutoFill(value = OperationType.UPDATE)
    @Update("update setmeal set status=#{status}, update_user=#{updateUser}, update_time=#{updateTime} where id = #{id}")
    void updateStatus(Setmeal setmeal);

    @Select("select count(1) from setmeal_dish s left join dish d on s.dish_id = d.id where setmeal_id = #{id} " +
            "and d.status = 0")
    int countDishBySetmealId(Long id);

    @Delete("delete from setmeal where id in (#{id})")
    void delete(List<Long> ids);

    @Delete("delete from setmeal_dish where setmeal_id in (#{id})")
    void deleteDishBySetmealId(List<Long> ids);

    @Select("select count(1) from setmeal where name = #{name}")
    int countByName(@Param("name") String name);

    @Select("select count(1) from setmeal where status = 1 and id in (#{id})")
    int countStatusById(List<Long> ids);
}
