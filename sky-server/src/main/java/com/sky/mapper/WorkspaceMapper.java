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
    Integer getOrderCountByCondition(LocalDateTime startTime, LocalDateTime endTime, List<Integer> statusList);

    // 查询今日营业额度
    Map<String, Object> getTurnover(LocalDateTime startTime, LocalDateTime endTime, List<Integer> statusList);
}
