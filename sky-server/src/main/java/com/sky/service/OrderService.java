package com.sky.service;

import com.sky.result.Result;
import com.sky.vo.OrderVO;

public interface OrderService {
    OrderVO getById(Long id);
}
