package com.demo.example.aop.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * 切面拦截处理器
 *
 * @author Ji MingHao
 * @since 2022-04-08 11:35
 */
public class AopInvocationHandler implements InvocationHandler {
    private final Object target;
    private final Aspect aspect;

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
