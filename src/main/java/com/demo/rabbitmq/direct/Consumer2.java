package com.demo.rabbitmq.direct;

import com.demo.rabbitmq.RabbitMQUtil;
import com.rabbitmq.client.*;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;

public class Consumer2 {

    private static final Connection connection = RabbitMQUtil.getRabbitConnection();

    @SneakyThrows
    public static void receiveMessage() {
        assert connection != null;
        Channel channel = connection.createChannel();
        channel.basicQos(1);
        channel.exchangeDeclare("logs_direct", "direct");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, "logs_direct", "info");
        channel.queueBind(queueName, "logs_direct", "warning");
        channel.queueBind(queueName, "logs_direct", "error");

        channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println(" [x] Received_2 '" + message + "'");
            }
        });
    }

    public static void main(String[] args) {
        Consumer2.receiveMessage();
    }
}
