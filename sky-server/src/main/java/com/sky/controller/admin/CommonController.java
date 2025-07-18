package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.properties.AliOssProperties;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("admin/common")
public class CommonController {

    @Autowired
    private AliOssProperties aliOssProperties;

    @RequestMapping("/upload")
    public Result upload(MultipartFile file){
        AliOssUtil ossUtil = new AliOssUtil(
                aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName());
        try{
            // 使用UUID生成唯一文件名
            String fileName = UUID.randomUUID().toString();
            // 保留后缀
            String originalFilename = file.getOriginalFilename();
            String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
            ossUtil.upload(file.getBytes(), fileName + fileType);
        } catch (Exception e){
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
        return Result.success();
    }
}
