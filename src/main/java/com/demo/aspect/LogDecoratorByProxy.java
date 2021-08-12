package com.demo.aspect;

import com.demo.annotation.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LogDecoratorByProxy implements InvocationHandler {

    Object instance;
    List<String> logMethodNames;

    private LogDecoratorByProxy(Object instance) {
        List<String> logMethodNames = Arrays.stream(instance.getClass().getDeclaredMethods())
                .filter(method -> method.getAnnotation(Log.class) != null)
                .map(Method::getName)
                .collect(Collectors.toList());
        this.instance = instance;
        this.logMethodNames = logMethodNames;
    }

    public static Object getInstance(Object instance, Class<?> interfaceClass) {
        return Proxy.newProxyInstance(
                instance.getClass().getClassLoader(),
                new Class[]{interfaceClass},
                new LogDecoratorByProxy(instance));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object invoke;
        if (logMethodNames.size() > 0 && logMethodNames.contains(method.getName())) {
            System.out.println("this is running by dynamic proxy and params is " + args[0]);
            invoke = method.invoke(instance, args);
            System.out.println("this is running by dynamic proxy and result is " + invoke);
        } else {
            invoke = method.invoke(instance, args);
        }
        return invoke;
    }
}
