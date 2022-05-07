package com.demo.aspect;

/**
 * Created with IntelliJ IDEA.
 * 通过动态代理实现日志装饰
 *
 * @author Ji MingHao
 * @since 2022-04-29 13:34
 */
public interface LogProxyInterface {

    /**
     * 通过代理添加日志内容
     *
     * @param logContent 日志内容
     */
    void addLogByProxy(String logContent);
}
