package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created with IntelliJ IDEA.
 * DemoApplication
 *
 * @author Ji MingHao
 * @since 2022-04-29 13:34
 */
//@EnableSwagger2
@SpringBootApplication
public class DemoApplication {
    public static void main(String... args) {
        String[] params = {"--k1=v1", "--k1=v2"};
        SpringApplication.run(DemoApplication.class, params);
        System.out.println("================================= Spring Boot 启动成功！=================================");
    }
}
