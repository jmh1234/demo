package com.demo.aspect;

import com.demo.annotation.Log;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * 通过Cglib实现日志装饰
 *
 * @author Ji MingHao
 * @since 2022-04-29 13:34
 */
public class LogDecoratorByCglib implements MethodInterceptor {

    Object instance;
    List<String> logMethodNames;

    private LogDecoratorByCglib(Object instance) {
        this.logMethodNames = Arrays.stream(instance.getClass().getDeclaredMethods())
                .filter(method -> method.getAnnotation(Log.class) != null)
                .map(Method::getName)
                .collect(Collectors.toList());
        this.instance = instance;
    }

    public static Object getInstance(Class<?> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new LogDecoratorByCglib(clazz.getConstructor().newInstance()));
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object invoke;
        if (!logMethodNames.isEmpty() && logMethodNames.contains(method.getName())) {
            System.out.println("this is running by CGLIB and params is " + objects[0]);
            invoke = method.invoke(instance, objects);
            System.out.println("this is running by CGLIB and result is " + invoke);
        } else {
            invoke = method.invoke(instance, objects);
        }
        return invoke;
    }
}
