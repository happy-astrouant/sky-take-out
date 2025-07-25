package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface ReportMapper {

    // 查找商品中top10销量的商品，需要收集商品名-销量
    /*
    *
    * */
    List<Map<String, Object>> top10Dish(LocalDateTime begin, LocalDateTime end);

    List<Map<String, Object>> userStatistics(LocalDateTime begin, LocalDateTime end);

    @Select("select count(id) from user where create_time < #{begin}")
    Integer getUserNumBefore(LocalDateTime begin);

    List<Map<String, Object>> turnoverStatistics(LocalDateTime begin, LocalDateTime end);

    List<Map<String, Object>> orderStatistics(LocalDateTime begin, LocalDateTime end, Integer status);


    Integer getTotalOrderNum(LocalDateTime begin, LocalDateTime end, Integer status);
}
