package com.test.demo.example.aop.aop;

import java.lang.reflect.Method;

public class Pointcut {

    // 类名匹配模式 (正则表达式)
    private String classPattern;

    // 方法级的匹配模式 (正则表达式)
    private String methodPattern;

    public Pointcut(String classPattern, String methodPattern) {
        super();
        this.classPattern = classPattern;
        this.methodPattern = methodPattern;
    }

    // 简单的类名匹配
    public boolean matchClass(Class<?> clazz) {
        return clazz.getName().matches(classPattern);
    }

    // 简单的方法名称匹配
    public boolean matchMethod(Method m) {
        return m.getName().matches(methodPattern);
    }

    public String getClassPattern() {
        return classPattern;
    }

    public void setClassPattern(String classPattern) {
        this.classPattern = classPattern;
    }

    public String getMethodPattern() {
        return methodPattern;
    }

    public void setMethodPattern(String methodPattern) {
        this.methodPattern = methodPattern;
    }

}
