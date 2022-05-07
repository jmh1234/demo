package com.demo;

import com.DemoApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DemoApplication.class)
class SpringBootRabbitMqTest {

    @Resource
    private RabbitTemplate rabbitTemplate;

//    @Test
//    public void testTopic() {
//        rabbitTemplate.convertAndSend("topics", "user.save", "user.save 的路由信息");
//    }
//
//    @Test
//    public void testDirect() {
//        rabbitTemplate.convertAndSend("directs", "info", "发送info的key的路由信息");
//    }
//
//    @Test
//    public void testFanout() {
//        rabbitTemplate.convertAndSend("logs", "", "Fanout模型发送的消息");
//    }
//
//    @Test
//    public void testWork() {
//        for (int i = 0; i < 10; i++) {
//            rabbitTemplate.convertAndSend("work", "work 模型 " + i);
//        }
//    }

    @Test
    void testHello() {
        for (int i = 1; i <= 10; i++) {
            rabbitTemplate.convertAndSend("hello", "hello word " + i);
        }
    }
}
