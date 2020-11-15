package com.mergeplus.handler;

import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 项目名称：merge-plus
 * 类 名 称：Handle
 * 类 描 述：TODO
 * 创建时间：2020/10/21 11:45 上午
 * 创 建 人：chenyouhong
 */
public class CollectionHandler extends AbstractHandler {

    @Override
    public void doHandler(Object obj) {
        AbstractHandler handler = this.next;
        if (obj instanceof Collection) {
            Collection items = (Collection)obj;
            for (Object item: items) {
                handler.doHandler(item);
            }
        }

        if (handler != null) {
            handler.doHandler(obj);
        }
    }
    
}
