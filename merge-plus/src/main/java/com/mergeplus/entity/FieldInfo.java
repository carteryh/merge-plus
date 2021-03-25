package com.mergeplus.entity;

import lombok.Data;
import org.springframework.http.HttpMethod;

import java.lang.reflect.Method;

/**
 * 项目名称：merge-plus
 * 类 名 称：MergeCache
 * 类 描 述：TODO
 * 创建时间：2020/10/21 1:59 下午
 * 创 建 人：chenyouhong
 */
@Data
public class FieldInfo {

    private Class<? extends Object> clientBeanClazz;

    private Object clientBean;

    private Method method;

    private HttpMethod httpMethod;

    private String key;

    private String sourceKey;

    private String targetKey;

    private String cacheKey;

    private String url;

    boolean enabledCache;

}
