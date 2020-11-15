package com.cloud.user.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 项目名称：spring-cloud-service
 * 类 名 称：LogAspect
 * 类 描 述：TODO
 * 创建时间：2020/9/12 11:06 下午
 * 创 建 人：chenyouhong
 */
@Aspect
@Component
public class LogAspect {

//    @Pointcut("@annotation(com.cloud.user.service.*)")
    @Pointcut("execution(* com.cloud.user.controller.*.*(..))")
    public void aspect() {
    }

    @Before("aspect()")
    public void before(JoinPoint jp) throws Throwable {
        //...
        System.out.println("Before");
    }


    @Around("aspect()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        //...
        System.out.println("==========");
        Object returnValue =  point.proceed(point.getArgs());
        System.out.println("**********");
        //...
        return returnValue;
    }

    @After("aspect()")
    public void after(JoinPoint jp) throws Throwable {
        //...
        System.out.println("after");
    }


}
