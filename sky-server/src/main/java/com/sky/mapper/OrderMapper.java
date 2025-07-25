package com.sky.mapper;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.net.Inet4Address;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {


    OrderVO getById(Long id);

    void updateOrder(Orders order);

    List<Map<String, Object>> statistics();

    List<OrderVO> conditionSearch(OrdersPageQueryDTO dto);

    void insert(Orders orders);

    @Select("select * from orders where number = #{orderNumber}")
    Orders getByOrderNumber(String orderNumber);
}
