package com.mergeplus.annonation;


import feign.Request;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 项目名称：merge-plus
 * 类 名 称：GetMerge
 * 类 描 述：GetMerge合并
 * 创建时间：2020/10/20 1:45 下午
 * 创 建 人：chenyouhong
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@RequestMergeMapping(method = Request.HttpMethod.GET)
public @interface GetMerge {

    /**
     *
     * @return value
     */
    String value() default "";

}