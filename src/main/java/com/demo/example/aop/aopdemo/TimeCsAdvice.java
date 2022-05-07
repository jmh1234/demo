package com.demo.example.aop.aopdemo;

import com.demo.example.aop.aop.Advice;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * TimeCsAdvice
 *
 * @author Ji MingHao
 * @since 2022-04-08 11:35
 */
public class TimeCsAdvice implements Advice {

    @Override
    public Object invoke(Object target, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        long startTime = System.currentTimeMillis();
        // 执行原方法
        Object ret = method.invoke(target, args);
        long useTime = System.currentTimeMillis() - startTime;
        System.out.println("记录：" + target.getClass() + "." + method.getName() + " 耗时：" + (useTime / 1000) + " 秒");
        return ret;
    }
}
