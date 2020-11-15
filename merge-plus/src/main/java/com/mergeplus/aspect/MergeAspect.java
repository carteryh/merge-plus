package com.mergeplus.aspect;

import com.mergeplus.core.MergeCore;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 项目名称：merge-plus
 * 类 名 称：MergeAspect
 * 类 描 述：merge切面
 * 创建时间：2020/10/20 2:06 下午
 * 创 建 人：chenyouhong
 */
@Slf4j
@Aspect
@Component
public class MergeAspect {

    @Autowired
    private MergeCore mergeCore;

    public MergeAspect() {
    }

    @Pointcut("@annotation(com.mergeplus.annonation.Merge)")
    public void methodPointcut() {
    }

    @Around("methodPointcut()&&args(obj)")
    public Object interceptor(ProceedingJoinPoint pjp, Object obj) throws Throwable {
        System.out.println("******************************");
        mergeCore.mergeData(obj);
        pjp.proceed();
        return "null";
    }

}