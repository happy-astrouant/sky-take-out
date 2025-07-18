package com.sky.mapper;


import com.sky.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SetmealMapper {

    void update(Setmeal setmeal);
}
