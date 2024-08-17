package com.mergeplus.handler;

import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.mergeplus.cache.Cache;
import com.mergeplus.cache.MergeCacheManager;
import com.mergeplus.constant.Constants;
import com.mergeplus.entity.FieldInfo;
import com.mergeplus.entity.MergeInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 项目名称：merge-plus
 * 类 名 称：Handle
 * 类 描 述：TODO
 * 创建时间：2020/10/21 11:45 上午
 * 创 建 人：chenyouhong
 */
@Slf4j
@Component
public class ObjectHandler extends AbstractHandler {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

//    @Resource
//    private RedisTemplate redisTemplate;

    @Resource
    private RestTemplate restTemplate;

    @Override
    public void doHandler(Object obj) {
        Class<?> clazz = obj.getClass();
        Cache cache = MergeCacheManager.getInstance();
        MergeInfo mergeInfo = (MergeInfo)cache.get(clazz);

        if (mergeInfo == null) {
            return;
        }

        String json = JSON.toJSONString(obj);
        JSONObject result = new JSONObject();
        List<FieldInfo> merges = new ArrayList<>();
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(obj));
        for (FieldInfo fieldInfo: mergeInfo.getFieldList()) {
            //don't start cache
            if (!fieldInfo.isEnabledCache()) {
                merges.add(fieldInfo);
                continue;
            }

//            if (!StringUtils.hasText(fieldInfo.getCacheKey())) {
//                merges.add(fieldInfo);
//                continue;
//            }

            if (fieldInfo.getClientBean() != null) {
                FeignClient feignClient = fieldInfo.getClientBeanClazz().getAnnotation(FeignClient.class);
                String name = feignClient.name();
                //cache key is empty and key is empty, setting cache key
                if (!StringUtils.hasText(fieldInfo.getCacheKey())) {
                    String sb = fieldInfo.getMethod().getName();
//                    String sb = name +
//                            Constants.COLON +
//                            fieldInfo.getMethod().getName() +
//                            Constants.COLON +
//                            jsonObject.get(fieldInfo.getSourceKey());
                    fieldInfo.setCacheKey(sb);
                }
            }

            String redisKey = fieldInfo.getCacheKey();

            String sourceKey = (String)JSONPath.eval(json, fieldInfo.getSourcePath());
            if (fieldInfo.isParamRedisKeyEnabled()) {
                if (!StringUtils.hasText(sourceKey)) {
                    continue;
                }
                redisKey = redisKey + Constants.DOUBLE_COLON + sourceKey;
            }

            String jsonValue = stringRedisTemplate.opsForValue().get(redisKey);
            Object value = JSON.parse(jsonValue);
            if (value == null || !(value instanceof JSONArray)) {
                merges.add(fieldInfo);
                continue;
            }

            //cacheObject
            if (value instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) value;
                if (jsonArray.size() <= 1) {
                    merges.add(fieldInfo);
                    continue;
                }

                Object v = jsonArray.getJSONObject(1).get(sourceKey);
                if (v == null) {
                    merges.add(fieldInfo);
                    continue;
                }
                JSONPath.set(obj, fieldInfo.getTargetPath(), v);
            } else if(value instanceof Map) {
                Map map = (Map) value;

                Object v = map.get(sourceKey);
                if (v == null) {
                    merges.add(fieldInfo);
                    continue;
                }
                JSONPath.set(obj, fieldInfo.getTargetPath(), v);
            }
        }

        // 设置子线程共享
        final RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

        merges.stream().map(e -> CompletableFuture.supplyAsync(() -> {
            JSONObject returnMap = null;
            try {
                // 设置当前线程的 RequestAttributes
                RequestContextHolder.setRequestAttributes(attributes);

                String sourceValue = (String)JSONPath.eval(json, e.getSourcePath());
                if (!StringUtils.hasText(sourceValue)) {
                    return e;
                }

                // 在取一次缓存
                String redisKey = e.getCacheKey();
                if (e.isParamRedisKeyEnabled()) {
                    if (!StringUtils.hasText(sourceValue)) {
                        return e;
                    }
                    redisKey = redisKey + Constants.DOUBLE_COLON + sourceValue;
                }

                String jsonValue = stringRedisTemplate.opsForValue().get(redisKey);
                Object value = JSON.parse(jsonValue);
                if (value != null) {
                    //cacheObject
                    if (value instanceof JSONArray) {
                        JSONArray jsonArray = (JSONArray) value;
                        if (jsonArray.size() > 1) {
                            Object v = jsonArray.getJSONObject(1).get(sourceValue);
                            if (v != null) {
                                JSONPath.set(obj, e.getTargetPath(), v);
                                return e;
                            }
                        }
                    } else if(value instanceof Map) {
                        Map map = (Map) value;

                        Object v = map.get(sourceValue);
                        if (v != null) {
                            JSONPath.set(obj, e.getTargetPath(), v);
                            return e;
                        }
                    }
                }

                if (e.getClientBean() != null) {
                    Object returnValue = e.getMethod().invoke(e.getClientBean(), sourceValue);
                    if (returnValue == null) {
                        return e;
                    }
                    returnMap = JSON.parseObject(JSON.toJSONString(returnValue));

                    if (returnMap == null || returnMap.isEmpty()) {
                        return e;
                    }
                    JSONPath.set(obj, e.getTargetPath(), returnMap.get(sourceValue));
                } else {
                    if (e.getUrl() == null || e.getUrl().trim().length() == 0) {
                        return e;
                    }

                    //设置Http的Header
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    //设置访问参数
                    HashMap<String, Object> params = new HashMap<>();
//                    if (e.getKey() == null || e.getKey().trim().length() == 0) {
//                        params.put(e.getSourceKey(), sourceValue);
//                    }

                    if (e.isParamRedisKeyEnabled()) {
                        params.put(e.getSourceKey(), sourceValue);
                    }

                    //设置访问的Entity
                    HttpEntity entity = new HttpEntity<>(params, headers);

                    // todo
                    ResponseEntity<Object> exchange = restTemplate.exchange(e.getUrl(), HttpMethod.GET, entity, Object.class);
                    if (exchange == null) {
                        return e;
                    }

                    Object body = exchange.getBody();
                    if (body == null) {
                        return e;
                    }

                    returnMap = JSON.parseObject(JSON.toJSONString(body));
                    if (returnMap == null || returnMap.isEmpty()) {
                        return e;
                    }

                    JSONPath.set(obj, e.getTargetPath(), returnMap.get(sourceValue));
                }
            } catch (Exception ex) {
                log.error("class: {}, methodName={}, error: {}", e.getClientBeanClazz(), e.getMethod().getName(), ex);
            } finally {
                RequestContextHolder.resetRequestAttributes(); // 记得在最后重置请求属性
            }
            return e;
        }, executor)).toList().stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    /**
     * 反射set
     * @param targetObject  目标对象
     * @param propertyName  属性名称
     * @param propertyValue 属性值
     */
    public static void setProperty(Object targetObject, String propertyName, Object propertyValue) {
        try {
            // 获取目标对象的 Class 对象
            Class<?> clazz = targetObject.getClass();

            // 获取要设置的字段 Field 对象
            Field field = clazz.getDeclaredField(propertyName);

            // 设置私有字段可以访问
            field.setAccessible(true);

            // 设置字段的值
            field.set(targetObject, propertyValue);
        } catch (Exception e) {
            log.error("setProperty error: {}",  e);
        }
    }

}
