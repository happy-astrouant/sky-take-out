package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingMapper {

    List<ShoppingCart> list(ShoppingCart cart);

    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumberById(ShoppingCart cart);


    void save(ShoppingCart cart);

    @Delete("delete from shopping_cart where user_id = #{userId}")
    void clean(Long userId);

    @Select("select * from shopping_cart")
    List<ShoppingCart> listAll();

    void delete(ShoppingCart cart);
}
