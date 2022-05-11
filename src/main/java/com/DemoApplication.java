package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created with IntelliJ IDEA.
 * DemoApplication
 *
 * @author Ji MingHao
 * @since 2022-04-29 13:34
 */
@SpringBootApplication
public class DemoApplication {

    public static void main(String... args) {
        SpringApplication.run(DemoApplication.class, "--k1=v1,v2");
        System.out.println("===========> Spring Boot 启动成功！");
    }
}
