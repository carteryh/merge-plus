package com.mergeplus.annonation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 项目名称：merge-plus
 * 类 名 称：MergeResult
 * 类 描 述：TODO
 * 创建时间：2020/10/21 10:26 上午
 * 创 建 人：chenyouhong
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface MergeResult {
//    Class<? extends IMergeResultParser> resultParser() default DefaultMergeResultParser.class;
}