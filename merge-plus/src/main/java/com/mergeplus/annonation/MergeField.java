package com.mergeplus.annonation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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

    /**
     *
     * @return key
     */
    String key() default "";

    /**
     *
     * @return client
     */
    Class<? extends Object> client() default Object.class;

    /**
     *
     * @return 数据源
     */
    String sourceKey() default "";

    /**
     *
     * @return 反射调用方法
     */
    String method() default "";

    /**
     *
     * @return 是否合并
     */
    boolean isMerge() default false;

    /**
     *
     * @return 缓存
     */
    String cache() default "";

    /**
     *
     * @return 是否启用缓存
     */
    boolean enabledCache() default true;

    /**
     *
     * @return 是否启用参数作为缓存key
     */
    boolean paramRedisKeyEnabled() default true;

}