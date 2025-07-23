package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("admin/workspace")
public class WorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;

    @GetMapping("/businessData")
    public Result businessData(){
        BusinessDataVO  businessDataVO = workspaceService.getBusinessData();
        return Result.success(businessDataVO);
    }
}
