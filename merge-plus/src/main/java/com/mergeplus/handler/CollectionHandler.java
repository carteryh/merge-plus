package com.mergeplus.handler;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 项目名称：merge-plus
 * 类 名 称：Handle
 * 类 描 述：TODO
 * 创建时间：2020/10/21 11:45 上午
 * 创 建 人：chenyouhong
 */
public class CollectionHandler extends AbstractHandler {

    /**
     *
     * @param obj 参数
     */
    @Override
    public void doHandler(Object obj) {
        AbstractHandler handler = this.next;
        if (obj instanceof Collection) {
            Collection temp = (Collection)obj;

            List<Object> items = Arrays.stream(temp.toArray()).toList();

            // 设置子线程共享
            final RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

            items.stream().map(item -> CompletableFuture.supplyAsync(() -> {
                // 设置当前线程的 RequestAttributes
                RequestContextHolder.setRequestAttributes(attributes);

                handler.doHandler(item);

                RequestContextHolder.resetRequestAttributes(); // 记得在最后重置请求属性
                return item;
            }, executor)).toList().stream().map(CompletableFuture::join).collect(Collectors.toList());
        }

        if (handler != null) {
            handler.doHandler(obj);
        }
    }
    
}
