package com.mirror;

import com.mirror.helper.ReflectionHelper;
import com.mirror.wrapping.ThrowableWrapper;

import java.lang.reflect.Proxy;

public class Mirror<T> {

    private Class<?> mTargetClass;
    private Class<T> mMirrorClass;
    private ReflectionHelper mReflectionHelper;
    private ThrowableWrapper mThrowableWrapper;

    Mirror(Class<T> mirrorClass, Class<?> targetClass, ReflectionHelper reflectionHelper, ThrowableWrapper throwableWrapper) {
        mMirrorClass = mirrorClass;
        mTargetClass = targetClass;
        mReflectionHelper = reflectionHelper;
        mThrowableWrapper = throwableWrapper;
    }

    public T create(Object instance) {
        if (!mTargetClass.isInstance(instance)) {
            throw new IllegalArgumentException("instance is not of targetClass type: " + mTargetClass.getName());
        }

        return createProxy(instance);
    }

    private T createProxy(Object instance) {
        return (T) Proxy.newProxyInstance(
                mMirrorClass.getClassLoader(),
                new Class[] {mMirrorClass},
                new MirrorInvocationHandler(mReflectionHelper, mThrowableWrapper, mTargetClass, instance));
    }
}
