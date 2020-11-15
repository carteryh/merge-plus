package com.cloud.user;

import com.cloud.UserApplication;
import com.mergeplus.core.MergeCore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目名称：merge-plus
 * 类 名 称：TestClass
 * 类 描 述：TODO
 * 创建时间：2020/10/20 5:40 下午
 * 创 建 人：chenyouhong
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = UserApplication.class) // 指定spring-boot的启动类
public class TestClass {

//    @Autowired
//    private TestService testService;

    @Autowired
    private MergeCore mergeCore;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;


    @Test
    public void test2() throws Exception {
        List<Object> objects = new ArrayList<>();

        Business business =  new Business();
        business.setValue("a");
        business.setValue2("c");
        business.setValue3("d");
        objects.add(business);

        Business business2 =  new Business();
        business2.setValue("c");
        business2.setValue2("b");
        business2.setValue3("x");
        objects.add(business2);

        mergeCore.mergeResult(objects);
        System.out.println(objects);
    }

    @Test
    public void test() throws Exception {

        HttpMethod[] values = HttpMethod.values();
        final List<String> collect = Arrays.stream(values).map(e -> e.name()).collect(Collectors.toList());


        String address="http://localhost:9938/dic/merge/{typeCode}";
        String s=address.replaceAll("\\{.*\\}", "test");
//        String s=address.replaceAll("\\(.*?\\)|\\)|（.*?）|）", "");

//        String s=address.replaceAll("\\{.*?\\}|\\}|（.*?）|）", "");

        final String s1 = HttpMethod.GET.toString();
        System.out.println(HttpMethod.GET.toString());



        Business businesstt =  null;
        Method method = StaticHttpClient.class.getMethod("merge", String.class);
        GetMapping mapping = method.getAnnotation(GetMapping.class);
        Type[] genericInterfaces = mapping.getClass().getGenericInterfaces();
        for (Type type : genericInterfaces) {
            RequestMapping requestMapping = (RequestMapping)((Class) type).getAnnotation(RequestMapping.class);
            RequestMethod[] requestMethods = requestMapping.method();
            System.out.println(requestMapping.method());
        }
//        final Annotation[] annotations2 = ((Class) mapping.getClass().getGenericInterfaces()[0]).getAnnotations();


        AnnotatedType[] annotatedInterfaces = mapping.getClass().getAnnotatedInterfaces();
        RequestMapping annotation1 = mapping.getClass().getAnnotation(RequestMapping.class);
        RequestMapping declaredAnnotation = mapping.getClass().getDeclaredAnnotation(RequestMapping.class);
        final Annotation[] annotations1 = method.getAnnotations();
        for (Annotation annotations : annotations1) {
            System.out.println(annotations);
        }

            String[] value = mapping.value();
//        PathVariable pathVariable = method.getAnnotation(PathVariable.class);

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        for (Annotation[] annotations : parameterAnnotations) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof PathVariable) {
                    PathVariable pathVariable = (PathVariable) annotation;
                    String name = pathVariable.name();
                    System.out.println(name);
                }
            }
        }

//        String[] parameterNames = getMethodParameterNamesByAnnotation(method);

//        String name = pathVariable.name();
//        final String value1 = pathVariable.value();

        List<Object> objects = new ArrayList<>();


    }


    public static void main(String[] args) throws Exception {





//
//        Business business =  new Business();
//        business.setFinanceChannel("MLJR");
//        Map<String, String> result = new HashMap();
//        result.put("financeChannelName", "test");
//        BeanUtils.copyProperties(business, result);
//        System.out.println(business);
//        business = null;
//        Assert.notNull(business, "error: prams t must not be null");
    }


}
