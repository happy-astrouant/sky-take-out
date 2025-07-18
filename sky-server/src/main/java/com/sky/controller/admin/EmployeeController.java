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
        employeeService.editPassword(map);
        return Result.success();
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

}
