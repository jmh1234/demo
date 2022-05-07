package com.demo.rabbitmq.pointtopoint;

import com.demo.rabbitmq.RabbitMqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;

/**
 * Created with IntelliJ IDEA.
 * Consumer2
 *
 * @author Ji MingHao
 * @since 2022-05-07 10:21
 */
public class Consumer2 {

    private static final Connection CONNECTION = RabbitMqUtil.getRabbitConnection();

    @SneakyThrows
    public static void receiveMessage() {
        assert CONNECTION != null;
        try (Channel channel = CONNECTION.createChannel()) {
            // 每次只能消费一个消息
            channel.basicQos(1);
            channel.queueDeclare("hello", true, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" + message + "'");
            };
            channel.basicConsume("hello", true, deliverCallback, consumerTag -> {

            });
        }
    }

    public static void main(String[] args) {
        receiveMessage();
    }
}
