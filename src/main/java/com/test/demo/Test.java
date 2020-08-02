package com.test.demo;

import com.test.demo.annotation.Log;
import com.test.demo.aspect.AdviceByEnhance;

public class Test {
    public static void main(String[] args) throws Exception {
        Test test = AdviceByEnhance.getInstance(Test.class);
        test.doSomething();
    }

    @Log()
    private void doSomething() {
        System.out.println("i want to do something but i do not know how to do it !");
    }
}
