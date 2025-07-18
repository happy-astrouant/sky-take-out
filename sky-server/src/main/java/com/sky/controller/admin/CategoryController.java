package com.sky.controller.admin;


import com.sky.result.PageResult;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/category")
public class CategoryController {
    @GetMapping("/page")
    public Result<PageResult> page(Integer page, Integer pageSize, String name) {
        return null;
    }
}
