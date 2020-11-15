package com.mergeplus.annonation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

/**
 * 项目名称：merge-plus
 * 类 名 称：MergeField
 * 类 描 述：merge字段注解
 * 创建时间：2020/10/20 1:45 下午
 * 创 建 人：chenyouhong
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
public @interface MergeField {

    String key() default "";

    Class<? extends Object> feign() default Object.class;

    String sourceKey() default "";

    String method() default "";

    boolean isMerge() default false;

    String cache() default "";

}