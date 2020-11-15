package com.mergeplus.core;

import com.mergeplus.annonation.Merge;
import com.mergeplus.handler.AbstractHandler;
import com.mergeplus.handler.CollectionHandler;
import com.mergeplus.handler.ObjectHandler;
import com.mergeplus.handler.ValidateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;

/**
 * 项目名称：merge-plus
 * 类 名 称：MergeCore
 * 类 描 述：TODO
 * 创建时间：2020/10/21 10:36 上午
 * 创 建 人：chenyouhong
 */
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
                .addHandle(applicationContext.getBean(ObjectHandler.class))
                .build();
    }

    @Merge
    public void mergeResult(Object object) {
        System.out.println(object);
        System.out.println(object);

    }

    public void mergeData(Object object) {
        if (build == null) {
            return;
        }
        build.doHandler(object);
    }

}
