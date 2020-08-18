package com.demo.aspect;

import java.util.List;

public class DecoratorMain {
    public static void main(String[] args) throws Exception {
        System.out.println("---------------addAdviceByAnnotation---------------");
        System.out.println("---------------byteBuddy---------------");
        DecoratorService logDecoratorByByteBuddy = LogDecoratorByByteBuddy.getInstance(DecoratorService.class);
        logDecoratorByByteBuddy.addLogByProxy("byteBuddy");
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
        List<Object> list = cacheDecoratorByByteBuddy.queryDataWithCache(10);
        List<Object> list1 = cacheDecoratorByByteBuddy.queryDataWithCache(10);
        System.out.println("---------------QueryWithCache---------------");
        System.out.println(list);
        System.out.println(list1);
        System.out.println();

        List<Object> list2 = cacheDecoratorByByteBuddy.queryDataWithoutCache(10);
        List<Object> list3 = cacheDecoratorByByteBuddy.queryDataWithoutCache(10);
        System.out.println("---------------QueryWithoutCache---------------");
        System.out.println(list2);
        System.out.println(list3);
    }
}
