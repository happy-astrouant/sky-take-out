package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.CreateFailedException;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // 更新套餐表
        setmealMapper.update(setmeal);
        // 删除套餐和菜品关联表数据
        setmealMapper.deleteBySetmealId(setmealDTO.getId());
        // 重新插入套餐和菜品的关联数据
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(setmealDishes != null && !setmealDishes.isEmpty()){
            setmealDishes.forEach(dish -> dish.setSetmealId(setmealDTO.getId()));
            setmealMapper.insertSetmealDish(setmealDishes);
        }

    }

    @Override
    @Transactional(readOnly = true)
    public PageResult page(SetmealPageQueryDTO query) {

        PageHelper.startPage(query.getPage(), query.getPageSize());
        List<Setmeal> setmealList = setmealMapper.page(query);
        PageInfo<Setmeal> pageInfo = new PageInfo<>(setmealList);
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    @Transactional
    public void save(SetmealDTO setmealDTO) {
        // 需要先检查名称重复与否
        boolean exist = setmealMapper.countByName(setmealDTO.getName()) > 0;
        if(exist) throw new CreateFailedException(MessageConstant.SETMEAL_NAME_EXIST);
        // 先保存主表
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.insert(setmeal);
        if(setmeal.getId() == null){
            throw new RuntimeException("获取新套餐的主键ID失效");
        }
        //保存套餐-菜品关系表
        List<SetmealDish> list = setmealDTO.getSetmealDishes();
        if(list != null && !list.isEmpty()){
            list.forEach(setmealDish -> setmealDish.setSetmealId(setmeal.getId()));
            setmealMapper.insertSetmealDish(list);
        }
    }

    @Override
    @Transactional
    public void updateStatus(Integer status, Long id) throws SetmealEnableFailedException {
        // 需要检查是否包含未起售的菜品
        int count = setmealMapper.countDishBySetmealId(id);
        // 包含未启售菜品且尝试起售
        if(count>0 && status == 1) throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(status);
        setmeal.setId(id);
        setmealMapper.updateStatus(setmeal);
    }

    @Override
    @Transactional
    public void delete(List<Long> ids) {
        // 检查有无起售
        int count = setmealMapper.countStatusById(ids);
        if(count > 0) throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        setmealMapper.delete(ids);
        setmealMapper.deleteDishBySetmealId(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public SetmealVO getByIdWithDish(Long id) {
        // 先获取菜品
        Setmeal setmeal = setmealMapper.getById(id);
        List<SetmealDish> setmealDishes = setmealMapper.getSetmealDishBySetmealId(id);
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    @Override
    public List<Setmeal> getSetmealsByCategoryId(Integer categoryId) {
        return setmealMapper.getSetmealByCategoryId(categoryId);
    }


}
