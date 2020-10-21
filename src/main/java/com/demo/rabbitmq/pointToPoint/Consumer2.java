package com.demo.rabbitmq.pointToPoint;

import com.demo.rabbitmq.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;

public class Consumer2 {

    private static final Connection connection = RabbitMQUtil.getRabbitConnection();

    @SneakyThrows
    public static void receiveMessage() {
        assert connection != null;
        Channel channel = connection.createChannel();
        channel.basicQos(1); // 每次只能消费一个消息
        channel.queueDeclare("hello", true, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume("hello", true, deliverCallback, consumerTag -> {
        });
    }

    public static void main(String[] args) {
        receiveMessage();
    }
}
