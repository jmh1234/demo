package com.demo.example.aop.service;

import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * SpaPrincessImpl
 *
 * @author Ji MingHao
 * @since 2022-04-08 11:35
 */
@Service
public class SpaPrincessImpl implements SpaService {
    private final Random random = new Random();
    private static final int BOUND = 5;

    @Override
    public void aromaOilMessage(String customer) {
        try {
            TimeUnit.SECONDS.sleep(random.nextInt(BOUND));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(customer + "享受完aromaOilMessage服务！");
    }

    @Override
    public void rest() {
        try {
            TimeUnit.SECONDS.sleep(random.nextInt(BOUND));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this + "休息完！");
    }
}
