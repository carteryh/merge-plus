package com.cloud.oauth.controller;


import com.cloud.oauth.service.LoginService;
import com.cloud.oauth.util.AuthToken;
import com.cloud.oauth.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletResponse;
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
@RequestMapping("/user")
public class UserLoginController {

    @Autowired
    private LoginService loginService;

    @Value("${auth.clientId}")
    private String clientId;

    @Value("${auth.clientSecret}")
    private String clientSecret;

    private static final String GRAND_TYPE = "password";//授权模式 密码模式


    @Value("${auth.cookieDomain}")
    private String cookieDomain;

    //Cookie生命周期
    @Value("${auth.cookieMaxAge}")
    private int cookieMaxAge;


    /**
     * 密码模式  认证.
     *
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("/login")
    public Mono<AuthToken> login(String username, String password) {
        //登录 之后生成令牌的数据返回
        AuthToken authToken = loginService.login(username, password, clientId, clientSecret, GRAND_TYPE);


        //设置到cookie中
        saveCookie(authToken.getAccessToken());
        return Mono.just(authToken);
//        return new Mono<>(true, StatusCode.OK,"令牌生成成功",authToken);
    }

    private void saveCookie(String token){
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        CookieUtil.addCookie(response,cookieDomain,"/","Authorization",token,cookieMaxAge,false);
    }

    /**
     * 密码模式  认证.
     *
     * @return
     */
    @RequestMapping("/test")
    @PreAuthorize(value = "hasAnyAuthority('goods_list')")
    public Mono<String> login() {
        //登录 之后生成令牌的数据返回


        return Mono.just("test");
//        return new Mono<>(true, StatusCode.OK,"令牌生成成功",authToken);
    }

    /**
     * 密码模式  认证.
     *
     * @return
     */
    @RequestMapping("/test2")
//    @PreAuthorize("hasPermission('test8988989')")
    public Mono<String> login2() {
        //登录 之后生成令牌的数据返回
        System.out.println("rest2");
        return Mono.just("test2");
//        return new Mono<>(true, StatusCode.OK,"令牌生成成功",authToken);
    }


}
