package com.cloud.user.service.impl;

import com.cloud.user.service.TestService;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.ManagedBean;

/**
 * 项目名称：spring-cloud-service
 * 类 名 称：TestServiceImpl
 * 类 描 述：TODO
 * 创建时间：2020/8/29 9:13 下午
 * 创 建 人：chenyouhong
 */
@Service
//@Repository
//@ManagedBean
public class TestServiceImpl implements TestService {

    @Override
    public void test() {
        System.out.println("test");
    }

}

