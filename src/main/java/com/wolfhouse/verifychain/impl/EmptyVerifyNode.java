package com.wolfhouse.verifychain.impl;

/**
 * @author linexsong
 */
public final class EmptyVerifyNode<T> extends BaseVerifyNode<T> {
    public static <T> EmptyVerifyNode<T> of(T t) {
        return new EmptyVerifyNode<T>().target(t);
    }

    @Override
    public EmptyVerifyNode<T> target(T target) {
        super.target(target);
        return this;
    }
}
