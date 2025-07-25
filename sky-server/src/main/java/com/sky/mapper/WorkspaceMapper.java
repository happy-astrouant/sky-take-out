package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface WorkspaceMapper {

    // 查询本日新增的用户数量
    @Select("select count(*) from user where create_time >= #{startTime} and create_time < #{endTime}")
    Integer getNewUserCount(LocalDateTime startTime, LocalDateTime endTime);

    //查询本日订单总数量 与 状态为完成的订单的数量
    Integer getOrderCountByCondition(LocalDateTime startTime, LocalDateTime endTime, Integer status);

    // 查询今日营业额度
    Map<String, Object> getTurnover(LocalDateTime startTime, LocalDateTime endTime, Integer status);

    @Select("select status, count(*) as count from dish group by status")
    List<Map<String, Object>> overviewDishes();

    @Select("select status, count(*) as count from setmeal group by status")
    List<Map<String, Object>> overviewSetmeals();

    @Select("select status, count(*) as count from orders group by status")
    List<Map<String, Object>> overviewOrders();
}
