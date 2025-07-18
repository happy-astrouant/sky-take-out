package com.sky.controller.admin;


import com.sky.dto.SetmealDTO;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("admin/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /*
    * 修改套餐信息
    * */
    @PutMapping
    public Result update(SetmealDTO setmealDTO) {
        log.info("修改套餐内容：{}", setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();
    }


}
