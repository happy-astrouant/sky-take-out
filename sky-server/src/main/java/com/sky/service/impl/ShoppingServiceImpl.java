package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.exception.BaseException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingMapper;
import com.sky.service.ShoppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingServiceImpl implements ShoppingService {
    @Autowired
    private ShoppingMapper shoppingMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public void add(ShoppingCartDTO dto) {
        // 首先判断购物车中是否已经存在该菜品
        ShoppingCart cart = ShoppingCart.builder()
                .userId(BaseContext.getCurrentId())
                .dishId(dto.getDishId())
                .setmealId(dto.getSetmealId())
                .dishFlavor(dto.getDishFlavor())
                .build();
        List<ShoppingCart> list = shoppingMapper.list(cart);
        if(list == null || list.size() == 0){
            // 不存在该菜品，那么需要先去其他数据库中查询出该菜品的信息
            // 然后再存入到购物车中
            if(dto.getDishId()!=null){
                Dish dish = dishMapper.getById(dto.getDishId());
                cart.setName(dish.getName());
                cart.setImage(dish.getImage());
                cart.setAmount(dish.getPrice());
            } else if(dto.getSetmealId()!=null){
                Setmeal setmeal = setmealMapper.getById(dto.getSetmealId());
                cart.setName(setmeal.getName());
                cart.setImage(setmeal.getImage());
                cart.setAmount(setmeal.getPrice());
            } else{
                throw new BaseException("缺少套餐和菜品信息，插入购物车失败");
            }
            cart.setCreateTime(LocalDateTime.now());
            cart.setNumber(1);
            shoppingMapper.save(cart);
        } else {
            cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shoppingMapper.updateNumberById(cart);
        }
    }
}
