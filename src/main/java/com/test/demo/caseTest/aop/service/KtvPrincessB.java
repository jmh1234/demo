package com.test.demo.caseTest.aop.service;

import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class KtvPrincessB implements KtvService {
    private Random random = new Random();

    @Override
    public void momoSingMessage(String customer) {
        // 开始时间
        try {
            int bound = 5;
            TimeUnit.SECONDS.sleep(random.nextInt(bound));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(customer + "享受完momoSing服务！");
    }
}
