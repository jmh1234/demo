package com.demo.rabbitmq.direct;

import com.demo.rabbitmq.RabbitMQConfig;
import com.rabbitmq.client.*;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Consumer {

    private static final Connection connection = RabbitMQConfig.getRabbitConnection();

    @SneakyThrows(IOException.class)
    public static void receiveMessage() {
        assert connection != null;
        Channel channel = connection.createChannel();
        channel.basicQos(1);
        channel.exchangeDeclare("logs_direct", "direct");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, "logs_direct", "error");

        channel.basicConsume(queueName, false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println(" [x] Received_1 '" + message + "'");
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }

    public static void main(String[] args) {
        Consumer.receiveMessage();
    }
}
