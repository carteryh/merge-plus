package com.cloud.user.controller;



import com.cloud.user.service.TestService;
import com.cloud.user.util.AuthToken;
import com.cloud.user.util.CookieUtil;
import com.google.common.collect.Maps;
import com.jarvis.cache.annotation.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 描述
 *
 * @author www.itheima.com
 * @version 1.0
 * @package com.changgou.oauth.controller *
 * @since 1.0
 */
@RestController
//@Service
@RefreshScope
@RequestMapping("/user")
public class UserController {

//    @Value("${user.test}") //获取bean的属性
//    private String test;

//    @Value("${uuu}") //获取bean的属性
//    private String url;

    @Autowired
    private TestService testService;

//    @Autowired
    private Map<String, String> map;

    private List<String> list;

    /**
     * 密码模式  认证.
     *
     * @return
     */
    @RequestMapping("/test/{name}")
    public Mono<String> login(@PathVariable(name = "name") String name) {
        //登录 之后生成令牌的数据返回


//        System.out.println(test);

        testService.test();
//        return Mono.just(url);
//        return new Mono<>(true, StatusCode.OK,"令牌生成成功",authToken);
        return null;
    }

    /**
     * 密码模式  认证.
     *
     * @return
     */
    @RequestMapping("/test2")
    @PreAuthorize(value = "hasAnyAuthority('goods_list22')")
//    @PreAuthorize("hasPermission('test8988989')")
    public Mono<String> login2() {
        //登录 之后生成令牌的数据返回
        System.out.println("rest2");
        return Mono.just("test2");
//        return new Mono<>(true, StatusCode.OK,"令牌生成成功",authToken);
    }

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

}
