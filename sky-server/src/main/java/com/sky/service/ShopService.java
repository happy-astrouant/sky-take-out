package com.sky.service;

import com.sky.result.Result;

public interface ShopService {
    void changeStatus(Integer status);
    Integer getStatus();
}
