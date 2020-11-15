# merge-plus
​		merge-plus主要用于字典merge，根据对应的枚举值，merge出枚举值对应的含义。

## 一、背景		

​		merge-plus目的为了解决前端字典枚举值显示为实际含义的问题，比如H5、app等。在实际项目中，一般都有一个字典的公共服务，用于配置字典的相关信息，但在项目开发中，数据库一般存储对应的枚举值，不会存储对应中文含义(中文含义变化，数据库需要同步变化)。前端处理枚举值有2种方案:

​	1、前端开发人员根据后端服务返回的字典码对应的枚举值去字典服务获取相应的中文含义

​	2、后端服务统一处理，把相应的字典码对应的枚举值转化为对应的中文含义返回给前端，前端即可展示

​		 为了尽量减少前端工作量，即所有的枚举值中文含义在后端服务统一处理后返回给前端使用，比如在很多前端详情页面，枚举值需要显示为真实的含义，此时即可使用merge-plus解决这一问题。



## 二、merge-plus介绍

​		merge-plus支持feign直接调用，也支持http方式直接调用，当服务不在同一个注册中心时，feign直连调用也可，但是此时需要指定相应的url，此时指定url直连调用其实和http方式直接调用区别不大，仅仅只是feign主键封装了一层。http直接调用指定相应的url即可。

​		merge-plus支持缓存，目前主要是基于redis缓存，当命中缓存时，直接使用缓存数据，未命中缓存，远程获取。项目最初仅仅是为了方便字典而设计，但并不意味着仅仅只能使用于字典，只要响应结果和字典响应结果一致，也可使用，比如，对于一张表，都会记录操作人员，但是数据库中通常都是保存用户编码，此时也可以使用merge获取对应用户编码的中文名等。

​	merge-plus使用多线程merge，目的是为解决响应时间问题，提高效率，有更好的用户体验。



## 三、merge-plus使用

1、项目基于gradle打包，版本6.7。打包本地maven仓库或私服，引入相应jar包即可。

​		compile 'com.merge.plus:merge-plus:1.0-SNAPSHOT'	

2、若需要使用redis缓存，特别注意redis序列化

3、merge方式

​	a、feign merge

​	feign merge有2种情况，同一个注册中心，url可空。

​	@FeignClient(name = "user-center")

​	当不在同一个注册中心时，需要指定直连url，该方式和http方式类似

​	@FeignClient(name = "user-center", url = "http://localhost:9002")

```java
package com.cloud.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

// 不在同一个注册中心时，需要指定直连url
@FeignClient(name = "user-center", url = "http://localhost:9002")
public interface StaticFeignClient {
 
	  @GetMapping("/user/dic/merge/{typeCode}")
    Map<String, String> merge(@PathVariable(name = "typeCode", required = true) String typeCode);

}
```

​	b、http merge

​	http merge主要是为了解决项目中不使用feign而提供的，和feign的直连merge一样。因此必然需要指定远程调用地址。

```java
package com.cloud.user;

import com.mergeplus.annonation.GetMerge;
import com.mergeplus.annonation.HttpMerge;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

// 需要指定url地址
@HttpMerge(url = "http://localhost:9002")
public interface StaticHttpClient {
 
	  @GetMerge(value = "/user/dic/merge/{typeCode}")
    Map<String, String> merge(@PathVariable(name = "typeCode", required = true) String typeCode);
    
}
```

4、远程调用响应数据规范

​	  因为支持redis缓存，所以建议使用，提高效率，因为大多数情况下，字典肯定不会经常变更。建议字典缓存使用autoload-cache，autoload-cache相关使用此处不做介绍，相关文档地址：https://github.com/qiujiayu/AutoLoadCache

字典接口响应规范案例：

```java
    @GetMapping("/dic/merge/{typeCode}")
    @Cache(expire = 3600, key = "#args[0]")
    public Map<String, String> merge(@PathVariable(name = "typeCode", required = true) String typeCode) {
        Map<String, String> map = Maps.newHashMap();
        map.put("a", "A");
        map.put("b","B");
        map.put("c", "C");
        map.put("d","D");

        return map;
    }
```

响应结果规范：

```json
{
  "a": "A",
  "b": "B",
  "c": "C",
  "d": "D"
}
```

5、在需要merge的对象字段上加上相应的注解

```java
package com.cloud.user;

import com.mergeplus.annonation.MergeField;
import lombok.Data;

import java.io.Serializable;

@Data
public class Business implements Serializable {

    private static final long serialVersionUID = 1L;

    private String value;

  	//http merge, cache非必须
    @MergeField(key = "test", sourceKey = "value", client = StaticHttpClient.class, method = "merge", cache = "user-center:test")
    private String valueName;

    //feign merge, cache非必须,但是当cache为空时，实际cache的key为StaticFeignClient的应用名拼接冒号，再拼接上注解(@MergeField)中key
  	// @FeignClient(name = "user-center", url = "http://localhost:9002")
		// public interface StaticFeignClient
  	// cache为 user-center:test(应用名拼接冒号，再拼接上key)
    @MergeField(key = "test", sourceKey = "value", client = StaticFeignClient.class, method = "merge")
    private String valueName2;

  	//feign merge, cache非必须
    @MergeField(key = "test", sourceKey = "value", client = StaticFeignClient.class, method = "merge", cache = "user-center:test")
    private String valueName3;

}
```

6、merge调用

​	先注入核心merge Bean，然后调用即可，方法支持单个对象merge，也支持List merge。

```java
	//核心merge Bean注入    
	@Autowired
  private MergeCore mergeCore;

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
```

7、merge结果

```java
[Business(value=a, valueName=A, value2=c, valueName2=C, value3=d, valueName3=D), Business(value=c, valueName=C, value2=b, valueName2=B, value3=x, valueName3=null)]
```



## 四、详细使用

​		详细使用见demo中spring-cloud-service项目user-center中测试类TestClass。

## 五、注意事项

​		使用缓存时，需要注入redis序列化及AutoLoadCache序列化，可参考spring-cloud-service项目中RedisConfiguration及AutoLoadCacheConfig配置。



## 六、联系方式

若有其他建议和bug提出、加新功能、遇到问题项目相关问题等，请联系qq号: 824291336

