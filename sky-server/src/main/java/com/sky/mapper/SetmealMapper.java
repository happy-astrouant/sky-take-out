package com.sky.mapper;


import com.sky.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealMapper {

    void update(Setmeal setmeal);

    @Select("select count(1) from setmeal where category_id = #{categoryId}")
    int countByCategoryId(Long categoryId);
}
