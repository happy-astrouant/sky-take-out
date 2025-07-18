package com.sky.mapper;

import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    @Update("update employee set password = #{newPassword} where id = #{id} and password = #{oldPassword}")
    void editPassword(Integer id, String oldPassword, String newPassword);

//    @Select("select * from employee where name like concat('%',#{name},'%')")
    List<Employee> page(String name);

    void save(Employee employee);
}
