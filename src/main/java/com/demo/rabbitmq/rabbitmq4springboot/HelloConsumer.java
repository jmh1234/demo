package com.demo.rabbitmq.rabbitmq4springboot;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * HelloConsumer
 * 默认创建的队列是持久化的 非独占的 不自动删除的
 *
 * @author Ji MingHao
 * @since 2022-05-07 10:21
 */
@Component
public class HelloConsumer {

    @RabbitListener(containerFactory = "workListenerFactory",
            queuesToDeclare = @Queue("hello"))
    public void receive1(String message) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Message1 = " + message);
    }

    @RabbitListener(containerFactory = "workListenerFactory",
            queuesToDeclare = @Queue("hello"))
    public void receive2(String message) {
        System.out.println("Message2 = " + message);
    }
}
