package com.mergeplus.handler;

import java.util.Collection;

/**
 * 项目名称：merge-plus
 * 类 名 称：Handle
 * 类 描 述：TODO
 * 创建时间：2020/10/21 11:45 上午
 * 创 建 人：chenyouhong
 */
public class ValidateHandler extends AbstractHandler {

    /**
     *
     * @param obj 参数
     */
    @Override
    public void doHandler(Object obj) {
        if (obj == null) {
            return;
        }

        if (obj instanceof Collection) {
            Collection items = (Collection)obj;
            if (items.isEmpty()) {
                return;
            }
        }
        if (this.next != null) {
            this.next.doHandler(obj);
        }
    }
    
}
