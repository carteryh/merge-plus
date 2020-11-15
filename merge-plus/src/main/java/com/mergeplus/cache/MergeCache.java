package com.mergeplus.cache;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * 项目名称：merge-plus
 * 类 名 称：MergeCache
 * 类 描 述：TODO
 * 创建时间：2020/10/21 1:59 下午
 * 创 建 人：chenyouhong
 */
@Data
public class MergeCache {

    private Class<? extends Object> mergeBeanClazz;

    private Class<? extends Object> feignBeanClazz;

    private Object feignBean;

    private Method method;

    private Object args;

    private String sourceKey;

    private String targetKey;

    private String cacheKey;

}
