package com.demo.example.aop.aop;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * 切点
 *
 * @author Ji MingHao
 * @since 2022-04-08 11:35
 */
public class Pointcut {

    /**
     * 类名匹配模式 (正则表达式)
     */
    private String classPattern;

    /**
     * 方法级的匹配模式 (正则表达式)
     */
    private String methodPattern;

    public Pointcut(String classPattern, String methodPattern) {
        super();
        this.classPattern = classPattern;
        this.methodPattern = methodPattern;
    }

    /**
     * 简单的类名匹配
     *
     * @param clazz 需检验的类
     * @return 是否匹配
     */
    public boolean matchClass(Class<?> clazz) {
        return clazz.getName().matches(classPattern);
    }

    /**
     * 方法校验
     *
     * @param m 方法
     * @return 是否匹配
     */
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
