package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        log.info("员工退出");
        BaseContext.removeCurrentId();
        return Result.success();
    }

    /**
     * 修改密码
     *
     * @return
     */
    @PutMapping("/editPassword")
    public Result<String> editPassword(@RequestBody Map<String, Object> map) {
        log.info("员工修改密码：{}", map);
        boolean success = employeeService.editPassword(map);
        if(success){
            return Result.success("修改密码成功");
        } else {
            return Result.error("修改密码失败");
        }
    }

    /**
     * 分页查询员工信息
     */
    @GetMapping("/page")
    public Result<PageResult> page(
            @RequestParam Integer page,
            @RequestParam Integer pageSize,
            @RequestParam(defaultValue = "") String name
    ) {
        log.info("分页查询员工信息，页码{}，页大小{}，员工姓名{}", page, pageSize, name);
        PageResult pageResult = employeeService.page(page, pageSize, name);
        return Result.success(pageResult);
    }

    /**
     * 新增员工
     */
    @PostMapping
    public Result save(@RequestBody Employee employee) {
        log.info("新增员工，员工数据：{}", employee);
        employeeService.save(employee);
        return Result.success();
    }

    /**
     * 启动/禁用员工账号
     */
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        log.info("员工账号启动/禁用：{}", id);
        employeeService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * 根据ID查询员工
     * */
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id) {
        log.info("根据id查询员工：{}", id);
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * 编辑员工信息
     */
    @PutMapping
    public Result update(@RequestBody Employee employee) {
        log.info("编辑员工信息：{}", employee);
        employeeService.update(employee);
        return Result.success();
    }
}
