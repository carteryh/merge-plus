package com.mergeplus.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mergeplus.cache.Cache;
import com.mergeplus.cache.MergeCacheManager;
import com.mergeplus.constant.Constants;
import com.mergeplus.entity.FieldInfo;
import com.mergeplus.entity.MergeInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
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

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    @Override
    public void doHandler(Object obj) {
        Class<?> clazz = obj.getClass();
        String className = clazz.getName();
        Cache cache = MergeCacheManager.getInstance();
        MergeInfo mergeInfo = (MergeInfo)cache.get(className);

        if (mergeInfo == null) {
            return;
        }

        Map<Object, Object> result = new HashMap<>();
        List<FieldInfo> merges = new ArrayList<>();
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(obj));
        for (FieldInfo fieldInfo: mergeInfo.getFieldList()) {
            if (fieldInfo.getClientBean() != null) {
                FeignClient feignClient = fieldInfo.getClientBeanClazz().getAnnotation(FeignClient.class);
                String name = feignClient.name();
                if (fieldInfo.getCacheKey() == null || Constants.BLANK.equals(fieldInfo.getCacheKey())) {
                    StringBuffer sb = new StringBuffer();
                    sb.append(name);
                    sb.append(Constants.COLON);
                    sb.append(name);
                    sb.append(Constants.COLON);
                    sb.append(fieldInfo.getMethod().getName());
                    sb.append(Constants.COLON);
                    sb.append(jsonObject.get(fieldInfo.getSourceKey()));
                    fieldInfo.setCacheKey(sb.toString());
                }
            }
            if (fieldInfo.getCacheKey() == null || fieldInfo.getCacheKey().trim().length() == 0) {
                merges.add(fieldInfo);
                continue;
            }
            Object value = redisTemplate.opsForValue().get(fieldInfo.getCacheKey());
            if (value == null || !(value instanceof Map)) {
                merges.add(fieldInfo);
                continue;
            }

            //cacheObject
            Map map = (Map) value;
            Object o = null;
            if (map.containsKey(Constants.AUTO_LOAD_CACHE_KEY)) {
                o = map.get(Constants.AUTO_LOAD_CACHE_KEY);
            }

            if (o != null && o instanceof Map) {
                map = (Map)o;
            }

            Object v = map.get(jsonObject.get(fieldInfo.getSourceKey()));
            if (v == null) {
                merges.add(fieldInfo);
                continue;
            }
            result.put(fieldInfo.getTargetKey(), v);
        }

        merges.stream().map(e -> CompletableFuture.supplyAsync(() -> {
            Map<Object, Object> returnMap = null;
            try {
                if (e.getClientBean() != null) {
                    Object returnValue = e.getMethod().invoke(e.getClientBean(), jsonObject.get(e.getSourceKey()));
                    returnMap = (Map) returnValue;
                    if (returnValue != null && returnValue instanceof Map) {
                        returnMap = (Map) returnValue;
                    }

                    if (returnMap == null || returnMap.isEmpty()) {
                        return e;
                    }

                    result.put(e.getTargetKey(), returnMap.get(jsonObject.get(e.getSourceKey())));
                } else {
                    if (e.getUrl() == null || e.getUrl().trim().length() == 0) {
                        return e;
                    }

                    //设置Http的Header
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    //设置访问参数
                    HashMap<String, Object> params = new HashMap<>();
                    if (e.getKey() == null || e.getKey().trim().length() == 0) {
                        params.put(e.getSourceKey(), jsonObject.get(e.getSourceKey()));
                    }
                    //设置访问的Entity
                    HttpEntity entity = new HttpEntity<>(params, headers);

                    ResponseEntity<Object> exchange = restTemplate.exchange(e.getUrl(), e.getHttpMethod(), entity, Object.class);
                    if (exchange == null) {
                        return e;
                    }

                    Object body = exchange.getBody();
                    if (body == null) {
                        return e;
                    }

                    if (body instanceof Map) {
                        returnMap = (Map) body;
                        if (returnMap == null || returnMap.isEmpty()) {
                            return e;
                        }
                        result.put(e.getTargetKey(), returnMap.get(jsonObject.get(e.getSourceKey())));
                    }
                }
            } catch (Exception ex) {
                log.error("class: {}, methodName={}, error: {}", e.getClientBeanClazz(), e.getMethod().getName(), ex);
            }
            return e;
        }, executor)).collect(Collectors.toList()).stream().map(CompletableFuture::join).collect(Collectors.toList());

        if (result == null || result.isEmpty()) {
            return;
        }

        try {
            BeanUtils.copyProperties(obj, result);
        } catch (Exception e) {
            log.error("copyProperties error: {}",  e);
        }
    }

}
