package com.sky.mapper;

import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
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

    @Update("update employee set password = #{newPassword}, update_time = #{updateTime}, update_user = #{id} " +
            "where id = #{id} and password = #{oldPassword}")
    int editPassword(Integer id, String oldPassword, String newPassword, LocalDateTime updateTime);

//    @Select("select * from employee where name like concat('%',#{name},'%')")
    List<Employee> page(String name);

    void save(Employee employee);

    @Update("update employee set status = #{status}, update_time = #{now}, update_user = #{currentId}  " +
            "where id = #{id}")
    void updateStatus(Integer status, Long id, LocalDateTime now, Long currentId);

    @Select("select * from employee where id = #{id}")
    Employee getById(Long id);

    void update(Employee employee);
}
