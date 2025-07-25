package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingService {
    void add(ShoppingCartDTO dto);

    void clean();

    List<ShoppingCart> list();

    void sub(ShoppingCartDTO dto);
}
