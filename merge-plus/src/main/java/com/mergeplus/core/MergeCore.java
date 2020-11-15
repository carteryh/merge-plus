package com.mergeplus.core;

import com.mergeplus.annonation.Merge;
import com.mergeplus.annonation.MergeField;
import com.mergeplus.cache.Cache;
import com.mergeplus.cache.MergeCacheManager;
import com.mergeplus.entity.FieldInfo;
import com.mergeplus.entity.MergeInfo;
import com.mergeplus.handler.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：merge-plus
 * 类 名 称：MergeCore
 * 类 描 述：TODO
 * 创建时间：2020/10/21 10:36 上午
 * 创 建 人：chenyouhong
 */
@Slf4j
public class MergeCore {
//    AbstractHandler build = new AbstractHandler.Builder()
//            .addHandle(new ValidateHandler())
//            .addHandle(new CollectionHandler())
//            .addHandle(new ObjectHandler())
//            .build();

    private AbstractHandler build = null;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        build = new AbstractHandler.Builder()
                .addHandle(applicationContext.getBean(ValidateHandler.class))
                .addHandle(applicationContext.getBean(CollectionHandler.class))
                .addHandle(applicationContext.getBean(MergeInfoHandler.class))
                .addHandle(applicationContext.getBean(ObjectHandler.class))
                .build();
    }

    @Merge
    public void mergeResult(Object object) {

    }


    public void mergeData(Object object) {
        if (build == null) {
            Assert.notNull(object, "error: build must not be null");
            return;
        }
        build.doHandler(object);
    }

}
