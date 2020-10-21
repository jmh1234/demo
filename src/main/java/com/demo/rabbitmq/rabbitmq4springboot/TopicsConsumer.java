package com.demo.rabbitmq.rabbitmq4springboot;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TopicsConsumer {
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,
                    exchange = @Exchange(value = "topics", type = "topic"),
                    key = {"user.*"}
            )
    })
    public void receive1(String message) {
        System.out.println("Message1 = " + message);
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,
                    exchange = @Exchange(value = "topics", type = "topic"),
                    key = {"order.*"}
            )
    })
    public void receive2(String message) {
        System.out.println("Message2 = " + message);
    }
}
