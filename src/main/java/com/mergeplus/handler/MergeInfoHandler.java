package com.mergeplus.handler;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.mergeplus.annonation.MergeField;
import com.mergeplus.annonation.RequestMergeMapping;
import com.mergeplus.cache.Cache;
import com.mergeplus.cache.MergeCacheManager;
import com.mergeplus.constant.Constants;
import com.mergeplus.entity.FieldInfo;
import com.mergeplus.entity.MergeInfo;
import com.mergeplus.request.AbstractRequest;
import feign.Request;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

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

    List<String> fieldNames = Lists.newArrayList("id", "createTime", "updateTime", "serialVersionUID");

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
        Cache cache = MergeCacheManager.getInstance();
        if (cache.get(obj.getClass()) != null) {
            return;
        }
        traverse(obj, obj, null, null);
    }


    /**
     *  递归initFieldInfo
     * @param source 源数据
     * @param obj obj对象
     * @param parentNode 父节点
     * @param index 下标索引
     */
    public void traverse(Object source, Object obj, String parentNode, Integer index) {
        if (obj == null) {
            return;
        }

        Class<?> clazz = obj.getClass();
//        System.out.println("Object: " + clazz.getSimpleName());

        if (obj instanceof Collection<?>) {
            Object[] items = ((Collection<?>) obj).toArray();
            if (items.length == 0) {
                return;
            }
            for (int i = 0; i < items.length; i++) {
                traverse(source, items[i], parentNode, i);
            }
        } else {
            // 获取对象的所有字段（包括私有字段）
//            Field[] fields = clazz.getDeclaredFields();
            Field[] fields = getFields(clazz);
            for (Field field : fields) {
                try {
                    if (fieldNames.contains(field.getName()) || field.getType().equals(Date.class)) {
                        continue;
                    }
                    // 检查该字段是否是final的
                    if (Modifier.isFinal(field.getModifiers())) {
                        continue;
                    }

                    // 设置私有字段可访问
                    field.setAccessible(true);

                    // 获取字段的值
                    Object value = field.get(obj);

                    // 打印字段名和值（或类型）
//                    System.out.print("  " + field.getName() + ": ");
//                    if (value == null) continue;

                    if (field.getType().isArray()) {
                        // 这里可以扩展以处理数组
//                        System.out.println("Array of " + value.getClass().getComponentType().getSimpleName());
                        traverse(source, value, parentNode, null);
                    }
//                    else if (value instanceof Object[]) {
//                        // 处理对象数组
//                        System.out.println("Object array");
//                        traverse(value, null);
//                    }
                    else if (field.getType().equals(Collection.class)) {
                        // 处理集合（需要导入java.util.Collection）
                        traverse(source, value, field.getName(), null);
                    } else if (field.getType().isPrimitive() || isWrapperType(field.getType())) {
                        // 基本类型或包装类型
//                        System.out.println(value);
                        mergeInfo(source, obj, field, parentNode, index);
                    } else {
                        // 递归遍历子对象
                        traverse(source, value, field.getName(), null);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Field[] getFields(Class<?> clazz) {
        List<Field> items = new ArrayList<>();
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            if (fields.length != 0) {
                items.addAll(Arrays.stream(fields).toList());
            }
//            for (Field field : fields) {
//                field.setAccessible(true); // 设置私有字段为可访问
//                System.out.println(clazz.getName() + "." + field.getName() + " : " + field.getType());
//            }
            clazz = clazz.getSuperclass(); // 移至父类
        }

        return items.toArray(new Field[0]);
    }


    // 辅助方法：检查类是否是基本类型的包装类
    private boolean isWrapperType(Class<?> cls) {
        return cls.equals(Boolean.class) ||
                cls.equals(Character.class) ||
                cls.equals(Byte.class) ||
                cls.equals(Short.class) ||
                cls.equals(Integer.class) ||
                cls.equals(Long.class) ||
                cls.equals(Float.class) ||
                cls.equals(Double.class) ||
                cls.equals(Void.class) ||
                cls.equals(String.class); // 也可以将String视为特殊的包装类型
    }

    private void mergeInfo(Object source, Object obj, Field field, String parentNode, Integer index) {
        Cache cache = MergeCacheManager.getInstance();
        MergeInfo mergeInfo = (MergeInfo)cache.get(source.getClass());

        if (mergeInfo == null) {
            mergeInfo = new MergeInfo();
        }

        MergeField mergeField = field.getAnnotation(MergeField.class);
        if (mergeField == null) {
            return;
        }
        Assert.notNull(mergeField.client(),"client class don't is null!");

        FieldInfo fieldInfo = new FieldInfo();
        fieldInfo.setKey(mergeField.key());
        fieldInfo.setSourceKey(mergeField.sourceKey());
        fieldInfo.setTargetKey(field.getName());

        Class<?> clientClass = mergeField.client();
        Method method = null;
        try {
            Class<?> paramClass = field.getType();
            method = clientClass.getDeclaredMethod(mergeField.method(), paramClass);
            fieldInfo.setMethod(method);
        } catch (Exception e) {
            log.info("find method error: {}", e);
        }

        fieldInfo.setClientBeanClazz(mergeField.client());
        fieldInfo.setClientBean(applicationContext != null && applicationContext.containsBean(mergeField.client().getName()) ? applicationContext.getBean(mergeField.client()) : null);

        String cacheKey = mergeField.cache();
        FeignClient feignClient = fieldInfo.getClientBeanClazz().getAnnotation(FeignClient.class);
        if (feignClient != null) {
//                String name = feignClient.name();
//                if (StringUtils.hasText(mergeField.cache())) {

//                }
//                if (StringUtils.isEmpty(cacheKey) && StringUtils.isEmpty(fieldInfo.getKey())) {
//                    StringBuffer sb = new StringBuffer();
//                    sb.append(name);
//                    sb.append(Constants.COLON);
//                    sb.append(mergeField.method());
//                    sb.append(Constants.COLON);
//                    sb.append(fieldInfo.getKey());
//                    sb.append(jsonObject.get(fieldInfo.getSourceKey()));
//                    cacheKey = sb.toString();
//                }
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
                    Request.HttpMethod[] httpMethods = requestMapping.method();
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
                        url = url.replaceAll(Constants.REPLACE_BRACE_CONTENT_REGEX, fieldInfo.getKey());
                    }
                }
            }
            fieldInfo.setUrl(url);
        }

        if (index == null) {
            String node = StringUtils.hasLength(parentNode) ? (parentNode  + ".") : "";
            String targetPath = StringUtils.hasLength(fieldInfo.getTargetPath()) ? fieldInfo.getTargetPath() : "";
            String sourcePath = StringUtils.hasLength(fieldInfo.getSourcePath()) ? fieldInfo.getSourcePath() : "";

            fieldInfo.setTargetPath(targetPath + node + field.getName());
            fieldInfo.setSourcePath(sourcePath + node + fieldInfo.getSourceKey());
        } else {
            String node = StringUtils.hasLength(parentNode) ? parentNode : "";
            String targetPath = StringUtils.hasLength(fieldInfo.getTargetPath()) ? fieldInfo.getTargetPath() : "";
            String sourcePath = StringUtils.hasLength(fieldInfo.getSourcePath()) ? fieldInfo.getSourcePath() : "";

            fieldInfo.setTargetPath(targetPath + node + "[" + index + "]." + field.getName());
            fieldInfo.setSourcePath(sourcePath + node + "[" + index + "]." + fieldInfo.getSourceKey());
        }

        fieldInfo.setCacheKey(cacheKey);
        fieldInfo.setEnabledCache(mergeField.enabledCache());
        fieldInfo.setParamRedisKeyEnabled(mergeField.paramRedisKeyEnabled());
        mergeInfo.getFieldList().add(fieldInfo);

        cache.put(source.getClass(), mergeInfo);
    }


}
