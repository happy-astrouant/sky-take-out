package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AddressMapper {

    @Select("select * from address_book where user_id = #{id}")
    List<AddressBook> list(Long id);

    @Select("select * from address_book where id = #{id}")
    AddressBook getAddressById(Long id);

    @Select("select * from address_book where user_id = #{userId} and is_default = 1")
    AddressBook getDefault(Long userId);

    @Update("update address_book set is_default = #{isDefault} where #{id}")
    void setDefault(AddressBook addressBook);

    void save(AddressBook addressBook);

    void update(AddressBook addressBook);

    @Delete("delete from address_book where id = #{id}")
    void delete(Long id);
}
