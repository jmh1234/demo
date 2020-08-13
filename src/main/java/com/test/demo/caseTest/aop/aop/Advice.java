package com.test.demo.caseTest.aop.aop;

import java.lang.reflect.Method;

public interface Advice {

    Object invoke(Object target, Method method, Object[] args) throws Exception;
}
