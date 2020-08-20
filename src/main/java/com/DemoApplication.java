package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.demo.dao")
public class DemoApplication {
    public static void main(String... args) {
        SpringApplication.run(DemoApplication.class, args);
        System.out.println("SpringBoot 启动成功！");
    }
}
