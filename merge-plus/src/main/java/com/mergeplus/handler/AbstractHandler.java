package com.mergeplus.handler;

/**
 * 项目名称：merge-plus
 * 类 名 称：Handle
 * 类 描 述：TODO
 * 创建时间：2020/10/21 11:45 上午
 * 创 建 人：chenyouhong
 */
public abstract class AbstractHandler {

    protected AbstractHandler next;

    public abstract void doHandler(Object obj);

    public static class Builder {

        private AbstractHandler head;

        private AbstractHandler tail;

        public Builder addHandle(AbstractHandler abstractHandler) {
            if (this.head == null) {
                this.head = this.tail = abstractHandler;
            }
            this.tail.next = abstractHandler;
            this.tail = abstractHandler;
            return this;
        }

        public AbstractHandler build() {
            return this.head;
        }

    }

}
