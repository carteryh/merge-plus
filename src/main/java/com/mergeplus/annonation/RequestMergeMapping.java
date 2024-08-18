package com.mergeplus.annonation;

import feign.Request;
import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 * 项目名称：merge-plus
 * 类 名 称：RequestMergeMapping
 * 类 描 述：合并请求
 * 创建时间：2020/10/20 1:45 下午
 * 创 建 人：chenyouhong
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface RequestMergeMapping {

    /**
     *
     * @return HttpMethod
     */
    Request.HttpMethod[] method() default {};

}