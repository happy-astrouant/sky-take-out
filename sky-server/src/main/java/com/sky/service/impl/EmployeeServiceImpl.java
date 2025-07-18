package com.sky.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import com.sky.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Map;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }


        password = MD5Utils.md5(password);

        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }
        BaseContext.setCurrentId(employee.getId());
        //3、返回实体对象
        return employee;
    }

    @Override
    public boolean editPassword(Map<String,  Object> map) {
        String oldPassword = (String) map.get("oldPassword");
        Integer id = (Integer) map.get("empId");
        String password = (String) map.get("newPassword");
        String passwordMD5 = DigestUtils.md5DigestAsHex(password.getBytes());
        String oldPasswordMD5 = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
        LocalDateTime updateTime = LocalDateTime.now();
        int rows = employeeMapper.editPassword(id, oldPasswordMD5, passwordMD5, updateTime);
        return rows > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult page(Integer page,
                           Integer pageSize,
                           String name) {
        PageHelper.startPage(page, pageSize);
        PageResult pageResult = new PageResult();
        List<Employee> records = employeeMapper.page(name);
        PageInfo<Employee> pageInfo = new PageInfo<>(records);
        pageResult.setRecords(pageInfo.getList());
        pageResult.setTotal(pageInfo.getTotal());
        return pageResult;
    }

    @Override
    public void save(Employee employee) {
        // 设置创建和修改时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        // 设置默认密码
        employee.setPassword(MD5Utils.md5(PasswordConstant.DEFAULT_PASSWORD));
        // 设置默认状态
        employee.setStatus(StatusConstant.DISABLE);
        // 设置创建人和修改人
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.save(employee);
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        // 设置update时间
        employeeMapper.updateStatus(status, id, LocalDateTime.now(), BaseContext.getCurrentId());
    }

    @Override
    public Employee getById(Long id) {
        return employeeMapper.getById(id);
    }

    @Override
    public void update(Employee employee) {
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.update(employee);
    }

}
