package com.sky.service;

import com.alibaba.druid.sql.ast.statement.SQLForeignKeyImpl;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

import java.util.Map;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    boolean editPassword(Map<String, Object> map);

    PageResult page(Integer page, Integer pageSize, String name);

    void save(Employee employee);

    void startOrStop(Integer status, Long id);

    Employee getById(Long id);
}
