package com.mergeplus.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mergeplus.annonation.MergeField;
import com.mergeplus.cache.MergeCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 项目名称：merge-plus
 * 类 名 称：Handle
 * 类 描 述：TODO
 * 创建时间：2020/10/21 11:45 上午
 * 创 建 人：chenyouhong
 */
@Slf4j
public class ObjectHandler extends AbstractHandler {

    public final static Map<String, List<MergeCache>> cacheMap = new ConcurrentHashMap();

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private RedisTemplate redisTemplate;

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    @Override
    public void doHandler(Object obj) {
        Class<?> clazz = obj.getClass();
        String name = clazz.getName();
        List<MergeCache> caches = cacheMap.get(name);
        List<MergeCache> mergeCaches = new ArrayList<>();
        if (caches == null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field: fields) {
                MergeField mergeField = field.getAnnotation(MergeField.class);
                if (mergeField == null) {
                    continue;
                }
                MergeCache mergeCache = new MergeCache();
//                mergeCache.setMergeBeanClazz(clazz);

                Class<?> feignClass = mergeField.feign();
                try {
                    Method method = feignClass.getDeclaredMethod(mergeField.method(), mergeField.key().getClass());
                    mergeCache.setMethod(method);
                } catch (Exception e) {
                    log.info("new feignBean error: {}", e);
                }

                if (mergeField.feign() != null) {
                    mergeCache.setFeignBeanClazz(mergeField.feign());
                    mergeCache.setFeignBean(applicationContext != null ? applicationContext.getBean(mergeField.feign()) : null);
                }
                mergeCache.setArgs(mergeField.key());
                mergeCache.setCacheKey(mergeField.cache());
                mergeCache.setSourceKey(mergeField.sourceKey());
                mergeCache.setTargetKey(field.getName());
                mergeCaches.add(mergeCache);
            }
        }

        if (mergeCaches.isEmpty()) {
            return;
        }
        cacheMap.put(name, mergeCaches);

        Map<Object, Object> result = new HashMap<>();
        List<MergeCache> merges = new ArrayList<>();
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(obj));
        for (MergeCache mergeCache: caches) {
            Map<Object, Object> value = (Map)redisTemplate.opsForValue().get(mergeCache.getCacheKey());
            if (value != null) {
                Map<Object, Object> map = value.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
                result.put(mergeCache.getTargetKey(), map.get(jsonObject.get(mergeCache.getSourceKey())));
                continue;
            }
            merges.add(mergeCache);
        }

        merges.stream().map(e -> CompletableFuture.supplyAsync(() -> {
            try {
                e.getMethod().invoke(e.getFeignBean(), e.getArgs());
                Object value = redisTemplate.opsForValue().get(e.getCacheKey());
//                if (value != null && value.length() > 0) {
//                    result.put(e.getTargetKey(), value);
//                }
            } catch (Exception ex) {
                log.error(" fieldName={}, error: {}", e, ex);
            }
            return e;
        }, executor)).collect(Collectors.toList()).stream().map(CompletableFuture::join).collect(Collectors.toList());

        BeanUtils.copyProperties(result, obj);
    }

}
