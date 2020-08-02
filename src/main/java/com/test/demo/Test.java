package com.test.demo;

import com.test.demo.annotation.Log;
import com.test.demo.aspect.AdviceByEnhance;
import com.test.demo.aspect.CacheClassDecorator;
import com.test.demo.service.DataService;

import java.util.List;

public class Test {
    public static void main(String[] args) throws Exception {
        Test test = AdviceByEnhance.getInstance(Test.class);
        test.doSomething();

        DataService instance = CacheClassDecorator.getInstance(DataService.class);
        List<Object> list = instance.queryData(10);
        System.out.println(list);
    }

    @Log
    private void doSomething() {
        System.out.println("i want to do something but i do not know how to do it !");
    }
}
