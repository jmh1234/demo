package com.demo.example.aop.service;

import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * KtvPrincessImpl
 *
 * @author Ji MingHao
 * @since 2022-04-08 11:35
 */
@Service
public class KtvPrincessImpl implements KtvService {

    private final Random random = new Random();

    @Override
    public void momoSingMessage(String customer) {
        try {
            int bound = 5;
            TimeUnit.SECONDS.sleep(random.nextInt(bound));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(customer + "享受完momoSing服务！");
    }
}
