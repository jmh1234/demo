package com.test.demo.example.aop.service;

import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class SpaPrincessA implements SPAService {
    private Random random = new Random();
    private int bound = 5;

    @Override
    public void aromaOilMessage(String customer) {
        try {
            TimeUnit.SECONDS.sleep(random.nextInt(bound));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(customer + "享受完aromaOilMessage服务！");
    }

    @Override
    public void rest() {
        try {
            TimeUnit.SECONDS.sleep(random.nextInt(bound));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this + "休息完！");
    }
}
