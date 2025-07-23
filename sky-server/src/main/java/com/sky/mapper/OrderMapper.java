package com.sky.mapper;

import com.sky.dto.OrdersCancelDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrderMapper {


    OrderVO getById(Long id);

    void updateOrder(Orders order);
}
