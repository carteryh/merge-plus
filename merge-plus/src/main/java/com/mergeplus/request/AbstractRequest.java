package com.mergeplus.request;

import com.mergeplus.entity.FieldInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 项目名称：merge-plus
 * 类 名 称：AbstractRequest
 * 类 描 述：TODO
 * 创建时间：2020/11/5 1:33 下午
 * 创 建 人：chenyouhong
 */
@Slf4j
public abstract class AbstractRequest {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     *
     */
    public static Map<HttpMethod, AbstractRequest> requestMap = new ConcurrentHashMap<>();

    /**
     *
     * @return 返回
     */
    abstract public HttpMethod getType();

    /**
     *
     * @param fieldInfo 参数
     * @return 返回
     */
    abstract public String parseRquest(FieldInfo fieldInfo);

//    public void doHttpMerge(Class<?> clazz) {
//        MergeInfo mergeInfo = new MergeInfo();
//        List<FieldInfo> fieldList = new ArrayList<>();
//        Field[] fields = clazz.getDeclaredFields();
//        for (Field field: fields) {
//            MergeField mergeField = field.getAnnotation(MergeField.class);
//            if (mergeField == null) {
//                continue;
//            }
//            Assert.notNull(mergeField.client(),"client class don't is null!");
//
//            FieldInfo fieldInfo = new FieldInfo();
//
//            Class<?> clientClass = mergeField.client();
//            Method method = null;
//            try {
//                method = clientClass.getDeclaredMethod(mergeField.method(), mergeField.key().getClass());
//                fieldInfo.setMethod(method);
//            } catch (Exception e) {
//                log.info("find method error: {}", e);
//            }
//
//            fieldInfo.setClientBeanClazz(mergeField.client());
//            fieldInfo.setClientBean(applicationContext != null ? applicationContext.getBean(mergeField.client()) : null);
//
//            if (fieldInfo.getCacheKey() == null && fieldInfo.getCacheKey().trim().length() > 0) {
//                String cacheKey = null;
//                FeignClient feignClient = fieldInfo.getClientBeanClazz().getAnnotation(FeignClient.class);
//                if (feignClient != null) {
//                    String name = feignClient.name();
//                    cacheKey = name + Constants.COLON + fieldInfo.getKey();
//                    fieldInfo.setCacheKey(cacheKey);
//                } else {
//                    String url = Constants.BLANK;
//                    Annotation[] methodAnnotations = method.getAnnotations();
//                    RequestMergeMapping requestMapping = null;
//                    for (Annotation annotation : methodAnnotations) {
//                        Type[] genericInterfaces = annotation.getClass().getGenericInterfaces();
//                        for (Type type : genericInterfaces) {
//                            requestMapping = (RequestMergeMapping)((Class) type).getAnnotation(RequestMergeMapping.class);
//                            if (requestMapping == null) {
//                                continue;
//                            }
//                            HttpMethod[] httpMethods = requestMapping.method();
//                            if (httpMethods == null || httpMethods.length == 0) {
//                                fieldInfo.setHttpMethod(httpMethods[0]);
//                                AbstractRequest abstractRequest = AbstractRequest.requestMap.get(httpMethods[0]);
//                                if (abstractRequest != null) {
//                                    url = abstractRequest.parseRquest(fieldInfo);
//                                }
//                            }
//                        }
//                    }
//
//                    Assert.notNull(requestMapping,"request method don't is null!");
//                    Assert.notNull(url,"request url don't is null!");
//
//                    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
//                    for (Annotation[] annotations : parameterAnnotations) {
//                        StringBuffer sb = new StringBuffer(Constants.LEFT_BRACE);
//                        for (Annotation annotation : annotations) {
//                            if (annotation instanceof PathVariable) {
//                                PathVariable pathVariable = (PathVariable) annotation;
//                                String name = pathVariable.name();
//                                sb.append(name);
//                                sb.append(Constants.RIGHT_BRACE);
//                                url = url.replaceAll(sb.toString(), fieldInfo.getKey());
//                            }
//                        }
//                    }
//                    fieldInfo.setUrl(url);
//                }
//            }
//
//            fieldInfo.setKey(mergeField.key());
//            fieldInfo.setCacheKey(mergeField.cache());
//            fieldInfo.setSourceKey(mergeField.sourceKey());
//            fieldInfo.setTargetKey(field.getName());
//            fieldList.add(fieldInfo);
//        }
//        mergeInfo.setFieldList(fieldList);
//    }

}
