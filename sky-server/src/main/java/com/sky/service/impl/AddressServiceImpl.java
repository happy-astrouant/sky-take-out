package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressMapper;
import com.sky.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public List<AddressBook> list() {
        Long id = BaseContext.getCurrentId();
        return addressMapper.list(id);
    }

    @Override
    public AddressBook getById(Long id) {
        return addressMapper.getAddressById(id);
    }

    @Override
    public AddressBook getDefault() {
        return addressMapper.getDefault(BaseContext.getCurrentId());
    }

    @Override
    @Transactional
    public void setDefault(AddressBook addressBook) {
        AddressBook defaultAddress = addressMapper.getDefault(BaseContext.getCurrentId());
        if(defaultAddress != null){
            defaultAddress.setIsDefault(0);
            addressMapper.setDefault(defaultAddress);
        }
        addressBook.setIsDefault(1);

        addressMapper.setDefault(addressBook);

    }

    @Override
    public void save(AddressBook addressBook) {
        addressBook.setIsDefault(0);
        addressMapper.save(addressBook);
    }

    @Override
    public void update(AddressBook addressBook) {
        addressMapper.update(addressBook);
    }

    @Override
    public void delete(Long id) {
        addressMapper.delete(id);
    }
}
