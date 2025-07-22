package com.sky.mapper;

import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {


    OrderVO getById(Long id);
}
