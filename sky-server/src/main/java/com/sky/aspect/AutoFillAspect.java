package com.sky.aspect;


import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Slf4j
@Component
public class AutoFillAspect {

    // 定义一个切入点
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    void autoFillPointCut(){}

    /*
    * 对createTime、updateTime、createUser、updateUser进行自动填充
    *
    * */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        // 获取当前方法的注解
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        AutoFill autoFill = method.getAnnotation(AutoFill.class);
        // 获取当前注解中定义的操作类型
        OperationType value = autoFill.value();

        // 获取当前方法参数的值
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0){
            return;
        }
        // 规定第一个参数为所需操作的封装对象
        Object entity = args[0];


        LocalDateTime now = LocalDateTime.now();
        Long id = BaseContext.getCurrentId();

        try {
            // 完成共同操作，赋值updateTime和updateUser
            // 获取方法setUpdateTime
            Method setUpdateTime = entity.getClass().getDeclaredMethod(
                    AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            setUpdateTime.invoke(entity, now);
            // 获取方法setUpdateUser
            Method setUpdateUser = entity.getClass().getDeclaredMethod(
                    AutoFillConstant.SET_UPDATE_USER, Long.class);
            setUpdateUser.invoke(entity, id);


            // 对insert类型，还需要赋值createTime和createUser
            if(value == OperationType.INSERT){
                // 获取方法setCreateTime
                Method setCreateTime = entity.getClass().getDeclaredMethod(
                        AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                setCreateTime.invoke(entity, now);

                // 获取方法setCreateUser
                Method setCreateUser = entity.getClass().getDeclaredMethod(
                        AutoFillConstant.SET_CREATE_USER, Long.class);
                setCreateUser.invoke(entity, id);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
