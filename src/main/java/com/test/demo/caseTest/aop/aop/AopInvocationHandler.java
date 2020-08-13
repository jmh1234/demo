package com.test.demo.caseTest.aop.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class AopInvocationHandler implements InvocationHandler {
    private Object target;
    private Aspect aspect;

    public AopInvocationHandler(Object target, Aspect aspect) {
        super();
        this.target = target;
        this.aspect = aspect;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 如果是要增强的方法，则切面增强
        if (this.aspect.getPointcut().matchMethod(method)) {
            return this.aspect.getAdvice().invoke(target, method, args);
        }
        // 方法不需要增强，则直接执行方法
        return method.invoke(target, args);
    }
}
