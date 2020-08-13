package com.test.demo.aspect;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class LogDecoratorByCglib implements MethodInterceptor {

    Object instance;

    public LogDecoratorByCglib(Object instance) {
        this.instance = instance;
    }

    public static Object getInstance(Class<?> clazz) throws Exception {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new LogDecoratorByCglib(clazz.getConstructor().newInstance()));
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("this is running by CGLIB and params is " + objects[0]);
        Object invoke = method.invoke(instance, objects);
        System.out.println("this is running by CGLIB and result is " + invoke);
        return invoke;
    }
}
