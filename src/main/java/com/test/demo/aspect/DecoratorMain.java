package com.test.demo.aspect;

import java.util.List;

public class DecoratorMain {
    public static void main(String[] args) throws Exception {
        System.out.println("---------------byteBuddy---------------");
        DecoratorService logDecoratorByByteBuddy = LogDecoratorByByteBuddy.getInstance(DecoratorService.class);
        logDecoratorByByteBuddy.addLogByAnnotation();
        System.out.println();

        System.out.println("---------------proxy---------------");
        LogProxyInterface logProxyInterface = new DecoratorService();
        LogProxyInterface proxyHandler = (LogProxyInterface) LogDecoratorByProxy.getInstance(logProxyInterface, LogProxyInterface.class);
        proxyHandler.addLogByProxy("proxy");
        System.out.println();

        System.out.println("---------------CGLIB---------------");
        DecoratorService cglibInterceptor = (DecoratorService) LogDecoratorByCglib.getInstance(DecoratorService.class);
        cglibInterceptor.addLogByProxy("CGLIB");
        System.out.println();

        System.out.println("---------------Cache---------------");
        DecoratorService cacheDecoratorByByteBuddy = CacheDecorator.getInstance(DecoratorService.class);
        List<Object> list = cacheDecoratorByByteBuddy.queryData(10);
        List<Object> list1 = cacheDecoratorByByteBuddy.queryDataWithoutCache(10);
        System.out.println(list);
        System.out.println(list1);
    }
}
