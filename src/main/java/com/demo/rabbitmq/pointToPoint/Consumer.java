package com.demo.rabbitmq.pointToPoint;

import com.demo.rabbitmq.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Consumer {

    private static final Connection connection = RabbitMQUtil.getRabbitConnection();

    @SneakyThrows(IOException.class)
    public static void receiveMessage() {
        assert connection != null;
        Channel channel = connection.createChannel();
        channel.basicQos(1); // 每次只能消费一个消息
        channel.queueDeclare("hello", true, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "'");
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        channel.basicConsume("hello", false, deliverCallback, consumerTag -> {
        });
    }

    public static void main(String[] args) {
        receiveMessage();
    }
}
