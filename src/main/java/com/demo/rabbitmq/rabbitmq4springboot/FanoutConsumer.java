package com.demo.rabbitmq.rabbitmq4springboot;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * FanoutConsumer
 *
 * @author Ji MingHao
 * @since 2022-05-07 10:21
 */
@Component
public class FanoutConsumer {
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,
                    exchange = @Exchange(value = "logs", type = "fanout")
            )
    })
    public void receive1(String message) {
        System.out.println("Message1 = " + message);
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,
                    exchange = @Exchange(value = "logs", type = "fanout")
            )
    })
    public void receive2(String message) {
        System.out.println("Message2 = " + message);
    }
}
