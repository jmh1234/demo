package com.test.demo.aspect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class LogDecoratorByProxy implements InvocationHandler {

    Object instance;

    public LogDecoratorByProxy(Object instance) {
        this.instance = instance;
    }

    public static Object getInstance(Object instance, Class<?> interfaceClass) throws Exception {
        return Proxy.newProxyInstance(
                instance.getClass().getClassLoader(),
                new Class[]{interfaceClass},
                new LogDecoratorByProxy(instance));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("this is running by dynamic proxy and params is " + args[0]);
        Object invoke = method.invoke(instance, args);
        System.out.println("this is running by dynamic proxy and result is " + invoke);
        return invoke;
    }
}
