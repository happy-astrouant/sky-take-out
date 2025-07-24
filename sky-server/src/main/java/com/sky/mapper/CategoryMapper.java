package com.sky.mapper;


import com.sky.annotation.AutoFill;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import com.sky.result.PageResult;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CategoryMapper {

    @Select("select type from category where id = #{id}")
    Integer getTypeById(Long id);

    List<Category> page(String name, Integer type);


    @Update("update category set name = #{name}, sort = #{sort}, type = #{type}," +
            "update_time = #{updateTime}, update_user = #{updateUser} " +
            "where id = #{id}")
    @AutoFill(value = OperationType.UPDATE)
    void update(Category category);

    @Update("update category set status = #{status}, " +
            "update_time = #{updateTime}, update_user = #{updateUser} where id = #{id}")
    @AutoFill(value = OperationType.UPDATE)
    void updateStatus(Category category);

    @AutoFill(value = OperationType.INSERT)
    void save(Category category);

    @Delete("delete from category where id = #{id}")
    void delete(Long id);

    @Select("select * from category where type = #{type}")
    List<Category> list(@Param("type") Integer type);

    @Select("select count(1) from category where name = #{name}")
    int countByName(@Param("name") String name);

    @Select("select * from category")
    List<Category> listAll();
}
