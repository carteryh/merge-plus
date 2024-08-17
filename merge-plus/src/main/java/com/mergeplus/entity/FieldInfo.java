package com.mergeplus.entity;

import feign.Request;
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
public class FieldInfo {

    private Class<? extends Object> clientBeanClazz;

    private Object clientBean;

    private Method method;

    private Request.HttpMethod httpMethod;

    private String key;

    private String sourceKey;

    private String targetKey;

    private String cacheKey;

    private String url;

    private String sourcePath;

    private String targetPath;

    boolean enabledCache;

    boolean paramRedisKeyEnabled;

    /**
     *
     * @return 返回
     */
    public Class<? extends Object> getClientBeanClazz() {
        return clientBeanClazz;
    }

    /**
     *
     * @param clientBeanClazz 参数
     */
    public void setClientBeanClazz(Class<? extends Object> clientBeanClazz) {
        this.clientBeanClazz = clientBeanClazz;
    }

    /**
     *
     * @return 返回
     */
    public Object getClientBean() {
        return clientBean;
    }

    /**
     *
     * @param clientBean 参数
     */
    public void setClientBean(Object clientBean) {
        this.clientBean = clientBean;
    }

    /**
     *
     * @return 返回
     */
    public Method getMethod() {
        return method;
    }

    /**
     *
     * @param method 参数
     */
    public void setMethod(Method method) {
        this.method = method;
    }

    /**
     *
     * @return 返回
     */
    public Request.HttpMethod getHttpMethod() {
        return httpMethod;
    }

    /**
     *
     * @param httpMethod 参数
     */
    public void setHttpMethod(Request.HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    /**
     *
     * @return 返回
     */
    public String getKey() {
        return key;
    }

    /**
     *
     * @param key 参数
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     *
     * @return 返回
     */
    public String getSourceKey() {
        return sourceKey;
    }

    /**
     *
     * @param sourceKey 参数
     */
    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }

    /**
     *
     * @return 返回
     */
    public String getTargetKey() {
        return targetKey;
    }

    /**
     *
     * @param targetKey 参数
     */
    public void setTargetKey(String targetKey) {
        this.targetKey = targetKey;
    }

    /**
     *
     * @return 返回
     */
    public String getCacheKey() {
        return cacheKey;
    }

    /**
     *
     * @param cacheKey 参数
     */
    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    /**
     *
     * @return 返回
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url 参数
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *
     * @return 返回
     */
    public boolean isEnabledCache() {
        return enabledCache;
    }

    /**
     *
     * @param enabledCache 参数
     */
    public void setEnabledCache(boolean enabledCache) {
        this.enabledCache = enabledCache;
    }

}
