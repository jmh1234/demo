package com.test.demo.aspect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogDecoratorByProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
