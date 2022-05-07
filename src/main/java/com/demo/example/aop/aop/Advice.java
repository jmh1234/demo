package com.demo.example.aop.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * 通知
 *
 * @author Ji MingHao
 * @since 2022-04-08 11:35
 */
public interface Advice {

    /**
     * 拦截方法
     *
     * @param target 目标
     * @param method 方法
     * @param args   数组
     * @return Object
     */
    Object invoke(Object target, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException;
}
