package com.test.demo.caseTest.aop.aopdemo;

import com.test.demo.caseTest.aop.aop.Advice;

import java.lang.reflect.Method;

public class TimeCsAdvice implements Advice {

    @Override
    public Object invoke(Object target, Method method, Object[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        // 执行原方法
        Object ret = method.invoke(target, args);
        long useTime = System.currentTimeMillis() - startTime;
        System.out.println("记录：" + target.getClass() + "." + method.getName() + " 耗时：" + (useTime / 1000) + " 秒");
        return ret;
    }
}