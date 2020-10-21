package com.demo.rabbitmq.rabbitmq4springboot;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DirectsConsumer {
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,
                    exchange = @Exchange(value = "directs"),
                    key = {"error"}
            )
    })
    public void receive1(String message) {
        System.out.println("Message1 = " + message);
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,
                    exchange = @Exchange(value = "directs"),
                    key = {"error", "info", "warning"}
            )
    })
    public void receive2(String message) {
        System.out.println("Message2 = " + message);
    }
}
