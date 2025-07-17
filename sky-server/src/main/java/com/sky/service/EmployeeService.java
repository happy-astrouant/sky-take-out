package com.sky.service;

import com.alibaba.druid.sql.ast.statement.SQLForeignKeyImpl;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;

import java.util.Map;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void editPassword(Map<String, Object> map);
}
