package com.sky.utils;

import org.springframework.util.DigestUtils;

public class MD5Utils {
    public static String md5(String source) {
        return DigestUtils.md5DigestAsHex(source.getBytes());
    }
}
