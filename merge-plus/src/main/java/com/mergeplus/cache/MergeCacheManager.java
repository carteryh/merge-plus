package com.mergeplus.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 项目名称：merge-plus
 * 类 名 称：MergeCacheManager
 * 类 描 述：缓存管理
 * 创建时间：2020/10/21 1:59 下午
 * 创 建 人：chenyouhong
 */
public class MergeCacheManager<T> implements Cache<T> {

    /**
     *
     */
    public final Map<Object, T> cacheMap = new ConcurrentHashMap();

    /**
     *
     */
    public final static MergeCacheManager mergeCacheManager = new MergeCacheManager();

    /**
     *
     * @return 返回
     */
    public static Cache getInstance() {
        return mergeCacheManager;
    }

    @Override
    public void put(Object key, T value) {
        cacheMap.put(key, value);
    }

    @Override
    public <T> T get(Object key) {
        return (T)cacheMap.get(key);
    }

}
