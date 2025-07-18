package com.sky.mapper;


import com.sky.entity.Category;
import com.sky.result.PageResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CategoryMapper {


    List<Category> page(String name, Integer type);

    Integer count(String name, Integer type);

    @Update("update category set name = #{name}, sort = #{sort}, type = #{type}," +
            "update_time = #{updateTime}, update_user = #{updateUser} " +
            "where id = #{id}")
    void update(Category category);

    @Update("update category set status = #{status}, " +
            "update_time = #{updateTime}, update_user = #{updateUser} where id = #{id}")
    void updateStatus(Integer status, Long id, LocalDateTime updateTime, Long updateUser);

    void save(Category category);
}
