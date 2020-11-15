package com.mergeplus.handler;

import com.mergeplus.annonation.MergeField;
import com.mergeplus.annonation.RequestMergeMapping;
import com.mergeplus.cache.Cache;
import com.mergeplus.cache.MergeCacheManager;
import com.mergeplus.constant.Constants;
import com.mergeplus.entity.FieldInfo;
import com.mergeplus.entity.MergeInfo;
import com.mergeplus.request.AbstractRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 项目名称：merge-plus
 * 类 名 称：Handle
 * 类 描 述：TODO
 * 创建时间：2020/10/21 11:45 上午
 * 创 建 人：chenyouhong
 */
@Slf4j
@Component
public class MergeInfoHandler extends AbstractHandler {

    private final static String COLON = ":";

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void doHandler(Object obj) {
        Object object = obj;
        if (obj instanceof Collection) {
            Collection items = (Collection)obj;
            if (items.isEmpty()) {
                return;
            }

            Object[] objects = items.toArray();
            object = objects[0];
        }
        //init merge info
        initFieldInfo(object);

        if (this.next != null) {
            this.next.doHandler(obj);
        }
    }

    private void initFieldInfo(Object obj) {
        Class<?> clazz = obj.getClass();
        String className = clazz.getName();

        Cache cache = MergeCacheManager.getInstance();
        MergeInfo mergeInfo = (MergeInfo)cache.get(className);
        if (mergeInfo == null) {
            mergeInfo = new MergeInfo();
            mergeInfo.setClassName(className);
            List<FieldInfo> fieldList = new ArrayList<>();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field: fields) {
                MergeField mergeField = field.getAnnotation(MergeField.class);
                if (mergeField == null) {
                    continue;
                }
                Assert.notNull(mergeField.client(),"client class don't is null!");

                FieldInfo fieldInfo = new FieldInfo();
                fieldInfo.setKey(mergeField.key());
                fieldInfo.setSourceKey(mergeField.sourceKey());
                fieldInfo.setTargetKey(field.getName());

                Class<?> clientClass = mergeField.client();
                Method method = null;
                try {
                    method = clientClass.getDeclaredMethod(mergeField.method(), mergeField.key().getClass());
                    fieldInfo.setMethod(method);
                } catch (Exception e) {
                    log.info("find method error: {}", e);
                }

                fieldInfo.setClientBeanClazz(mergeField.client());
                fieldInfo.setClientBean(applicationContext != null && applicationContext.containsBean(mergeField.client().getName()) ? applicationContext.getBean(mergeField.client()) : null);

                if (fieldInfo.getCacheKey() == null || fieldInfo.getCacheKey().trim().length() > 0) {
                    String cacheKey = null;
                    FeignClient feignClient = fieldInfo.getClientBeanClazz().getAnnotation(FeignClient.class);
                    if (feignClient != null) {
                        String name = feignClient.name();
                        cacheKey = name + COLON + fieldInfo.getKey();
                        fieldInfo.setCacheKey(cacheKey);
                    } else {
                        String url = Constants.BLANK;
                        Annotation[] methodAnnotations = method.getAnnotations();
                        RequestMergeMapping requestMapping = null;
                        for (Annotation annotation : methodAnnotations) {
                            Type[] genericInterfaces = annotation.getClass().getGenericInterfaces();
                            for (Type type : genericInterfaces) {
                                requestMapping = (RequestMergeMapping) ((Class) type).getAnnotation(RequestMergeMapping.class);
                                if (requestMapping == null) {
                                    continue;
                                }
                                HttpMethod[] httpMethods = requestMapping.method();
                                if (httpMethods != null && httpMethods.length != 0) {
                                    fieldInfo.setHttpMethod(httpMethods[0]);
                                    AbstractRequest abstractRequest = AbstractRequest.requestMap.get(httpMethods[0]);
                                    if (abstractRequest != null) {
                                        url = abstractRequest.parseRquest(fieldInfo);
                                    }
                                }
                            }
                        }

                        Assert.notNull(requestMapping, "request method don't is null!");
                        Assert.notNull(url, "request url don't is null!");

                        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                        for (Annotation[] annotations : parameterAnnotations) {
                            for (Annotation annotation : annotations) {
                                if (annotation instanceof PathVariable) {
                                    PathVariable pathVariable = (PathVariable) annotation;
                                    String name = pathVariable.name();
                                    url = url.replaceAll(Constants.REPLACE_BRACE_CONTENT_REGEX, fieldInfo.getKey());
                                }
                            }
                        }

                        fieldInfo.setUrl(url);
                    }
                }
                fieldList.add(fieldInfo);
            }
            mergeInfo.setFieldList(fieldList);
        }

        if (mergeInfo.getFieldList() == null || mergeInfo.getFieldList().isEmpty()) {
            return;
        }
        cache.put(className, mergeInfo);
    }
    
}
