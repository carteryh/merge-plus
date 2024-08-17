package com.mergeplus.handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 项目名称：merge-plus
 * 类 名 称：Handle
 * 类 描 述：TODO
 * 创建时间：2020/10/21 11:45 上午
 * 创 建 人：chenyouhong
 */
public abstract class AbstractHandler {

    /**
     * 多线程
     */
    protected static final ExecutorService executor = Executors.newCachedThreadPool();

    /**
     *
     */
    protected AbstractHandler next;

    /**
     * 参数
     * @param obj 参数
     */
    public abstract void doHandler(Object obj);

    /**
     *
     */
    public static class Builder {

        private AbstractHandler head;

        private AbstractHandler tail;

        /**
         * 参数
         * @param abstractHandler 参数
         * @return 返回
         */
        public Builder addHandle(AbstractHandler abstractHandler) {
            if (this.head == null) {
                this.head = this.tail = abstractHandler;
            }
            this.tail.next = abstractHandler;
            this.tail = abstractHandler;
            return this;
        }

        /**
         *
         * @return 返回
         */
        public AbstractHandler build() {
            return this.head;
        }

    }

}
