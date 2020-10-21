package com.demo.rabbitmq.fanout;

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
        channel.basicQos(1); // 每次只能消费一个消息
        channel.exchangeDeclare("logs", "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, "logs", "");

        channel.basicConsume(queueName, false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println(" [x] Received1 '" + message + "'");
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }

    public static void main(String[] args) {
        Consumer.receiveMessage();
    }
}
