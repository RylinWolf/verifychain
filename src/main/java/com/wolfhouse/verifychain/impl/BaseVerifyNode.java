package com.wolfhouse.verifychain.impl;


import com.wolfhouse.verifychain.VerifyConstant;
import com.wolfhouse.verifychain.VerifyException;
import com.wolfhouse.verifychain.VerifyNode;
import com.wolfhouse.verifychain.VerifyStrategy;
import lombok.SneakyThrows;

import java.util.function.Predicate;

/**
 * @author linexsong
 */
public abstract class BaseVerifyNode<T> implements VerifyNode<T> {
    protected T t;
    protected Predicate<T> predicate;
    protected Exception customException;
    protected VerifyStrategy strategy = VerifyStrategy.WITH_CUSTOM_EXCEPTION;
    protected Boolean allowNull = false;

    public BaseVerifyNode() {
    }

    public BaseVerifyNode(T t) {
        this();
        this.t = t;
    }

    public BaseVerifyNode(T t, Boolean allowNull) {
        this(t);
        this.allowNull = allowNull;
    }

    @Override
    public BaseVerifyNode<T> predicate(Predicate<T> predicate) {
        this.predicate = predicate;
        return this;
    }

    @Override
    public BaseVerifyNode<T> exception(Exception e) {
        this.customException = e;
        return this;
    }

    @Override
    public BaseVerifyNode<T> exception(String message) {
        this.customException = new VerifyException(message);
        return this;
    }

    @Override
    public BaseVerifyNode<T> allowNull(Boolean allowNull) {
        this.allowNull = allowNull;
        return this;
    }

    @Override
    public boolean verify() {
        if (t == null) {
            return allowNull;
        }

        if (predicate != null) {
            return predicate.test(t);
        }

        return true;
    }

    @Override
    public boolean verify(Predicate<T> predicate) {
        return (this.allowNull == true && t == null) || predicate.test(t);
    }


    @SneakyThrows
    @Override
    public boolean verifyWithCustomE(Exception e) {
        this.customException = e;
        if (!verify()) {
            throw customException;
        }
        return true;
    }


    @Override
    public boolean verifyWithCustomE() {
        if (customException == null) {
            return verifyWithE();
        }
        return verifyWithCustomE(customException);
    }

    @Override
    public boolean verifyWithE() {
        return verifyWithCustomE(new VerifyException(VerifyConstant.VERIFY_FAILED));
    }

    @Override
    public BaseVerifyNode<T> setCustomException(Exception e) {
        this.customException = e;
        return this;
    }

    @Override
    public VerifyStrategy getStrategy() {
        return this.strategy;
    }

    @Override
    public BaseVerifyNode<T> setStrategy(VerifyStrategy strategy) {
        this.strategy = strategy;
        return this;
    }

    @Override
    public Exception getException() {
        return this.customException;
    }

    @Override
    public BaseVerifyNode<T> target(T target) {
        this.t = target;
        return this;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
