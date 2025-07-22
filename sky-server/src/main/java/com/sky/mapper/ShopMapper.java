package com.sky.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ShopMapper {
    @Select("select status from shop_status")
    Integer getStatus();

    @Update("update shop_status set status = #{status}")
    void updateStatus(Integer status);
}
