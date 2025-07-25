package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
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

    @Update("update employee set password = #{newPassword}, update_time = #{updateTime}, update_user = #{updateUser} " +
            "where id = #{id} and password = #{password}")
    @AutoFill(value = OperationType.UPDATE)
    int editPassword(Employee employee, String newPassword);

//    @Select("select * from employee where name like concat('%',#{name},'%')")
    List<Employee> page(String name);

    @AutoFill(value = OperationType.INSERT)
    void save(Employee employee);

    @Update("update employee set status = #{status}, update_time = #{updateTime}, update_user = #{updateUser}  " +
            "where id = #{id}")
    @AutoFill(value = OperationType.UPDATE)
    void updateStatus(Employee employee);

    @Select("select * from employee where id = #{id}")
    Employee getById(Long id);

    @AutoFill(value = OperationType.UPDATE)
    void update(Employee employee);

}
