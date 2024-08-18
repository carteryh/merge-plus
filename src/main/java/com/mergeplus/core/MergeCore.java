package com.mergeplus.core;

import com.mergeplus.annonation.Merge;
import com.mergeplus.handler.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 项目名称：merge-plus
 * 类 名 称：MergeCore
 * 类 描 述：TODO
 * 创建时间：2020/10/21 10:36 上午
 * 创 建 人：chenyouhong
 */
@Slf4j
@Component
public class MergeCore {
//    AbstractHandler build = new AbstractHandler.Builder()
//            .addHandle(new ValidateHandler())
//            .addHandle(new CollectionHandler())
//            .addHandle(new ObjectHandler())
//            .build();

    private AbstractHandler build = null;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 初始化责任链
     */
    @PostConstruct
    public void init() {
        build = new AbstractHandler.Builder()
                .addHandle(applicationContext.getBean(ValidateHandler.class))
                .addHandle(applicationContext.getBean(CollectionHandler.class))
                .addHandle(applicationContext.getBean(MergeInfoHandler.class))
                .addHandle(applicationContext.getBean(ObjectHandler.class))
                .build();
    }

    /**
     *
     * @param object 合并参数
     */
    @Merge
    public void mergeResult(Object object) {

    }

    /**
     *
     * @param object 合并参数
     */
    public void mergeData(Object object) {
        if (build == null) {
            Assert.notNull(object, "error: build must not be null");
            return;
        }
        build.doHandler(object);
    }

}
