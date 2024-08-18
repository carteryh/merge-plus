package com.mergeplus.cache;

import org.springframework.lang.Nullable;

/**
 * 项目名称：merge-plus
 * 类 名 称：Cache
 * 类 描 述：缓存
 * 创建时间：2020/10/24 11:01 下午
 * 创 建 人：chenyouhong
 */
public interface Cache<T> {

    /**
     *
     * @param key key
     * @param value param
     */
    void put(Object key, @Nullable T value);

    /**
     *
     * @param key key
     * @return 返回
     * @param <T> param
     */
    <T> T get(Object key);

}
