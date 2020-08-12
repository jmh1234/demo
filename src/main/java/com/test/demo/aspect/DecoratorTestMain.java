package com.test.demo.aspect;

import java.util.List;

public class DecoratorTestMain {
    public static void main(String[] args) throws Exception {
        DecoratorTestService decoratorTestMain = LogDecoratorByByteBuddy.getInstance(DecoratorTestService.class);
        decoratorTestMain.doSomething();

        DecoratorTestService instance = CacheDecoratorByByteBuddy.getInstance(DecoratorTestService.class);
        List<Object> list = instance.queryData(10);
        List<Object> list1 = instance.queryDataWithoutCache(10);
        System.out.println(list);
        System.out.println(list1);
    }
}
