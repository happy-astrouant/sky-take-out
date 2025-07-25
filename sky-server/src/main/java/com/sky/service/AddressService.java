package com.sky.service;

import com.sky.entity.AddressBook;
import com.sky.result.Result;

import java.util.List;

public interface AddressService {
    List<AddressBook> list();

    AddressBook getById(Long id);

    AddressBook getDefault();

    void setDefault(AddressBook addressBook);

    void save(AddressBook addressBook);

    void update(AddressBook addressBook);

    void delete(Long id);
}
