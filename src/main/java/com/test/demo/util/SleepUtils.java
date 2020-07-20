package com.test.demo.util;

import java.util.concurrent.TimeUnit;

public class SleepUtils {
    public static void second(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException");
        }
    }
}
