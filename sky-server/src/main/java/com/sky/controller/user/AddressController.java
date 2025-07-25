package com.sky.controller.user;


import com.sky.constant.CacheConstant;
import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
public class AddressController {

    @Autowired
    private AddressService addressService;

    // 查询用户的所有地址信息
    @GetMapping("/list")
    public Result<List<AddressBook>> list() {
        return Result.success(addressService.list());
    }

    //根据地址id查询地址的具体信息
    @GetMapping("/{id}")
    @Cacheable(value = CacheConstant.ADDRESS_CACHE, key = "#id")
    public Result<AddressBook> getById(@PathVariable Long id) {
        return Result.success(addressService.getById(id));
    }

    // 获取用户的默认地址
    @GetMapping
    @Cacheable(value = CacheConstant.DEFAULT_ADDRESS_CACHE, key = "#result.data.userId")
    public Result<AddressBook> getDefault() {
        return Result.success(addressService.getDefault());
    }

    // 设置默认地址
    @PutMapping("/default")
    @Caching(
        evict = {
            @CacheEvict(value = CacheConstant.ADDRESS_CACHE, allEntries = true),
            @CacheEvict(value = CacheConstant.DEFAULT_ADDRESS_CACHE, allEntries = true),
        }
    )
    public Result setDefault(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressService.setDefault(addressBook);
        return Result.success();
    }

    // 新增地址
    @PostMapping
    public Result save(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressService.save(addressBook);
        return Result.success();
    }

    // 修改地址
    @PutMapping
    @Caching(
        evict = {
            @CacheEvict(value = CacheConstant.ADDRESS_CACHE, key = "#addressBook.id"),
            @CacheEvict(value = CacheConstant.DEFAULT_ADDRESS_CACHE, key = "#addressBook.userId"),
        }
    )
    public Result update(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressService.update(addressBook);
        return Result.success();
    }

    // 删除地址
    @DeleteMapping("/")
    @Caching(
        evict = {
            @CacheEvict(value = CacheConstant.ADDRESS_CACHE, key = "#id"),
            @CacheEvict(value = CacheConstant.DEFAULT_ADDRESS_CACHE, allEntries = true),
        }
    )
    public Result delete(@RequestParam Long id) {
        addressService.delete(id);
        return Result.success();
    }


}
