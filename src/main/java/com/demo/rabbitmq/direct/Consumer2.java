package com.demo.rabbitmq.direct;

import com.demo.rabbitmq.RabbitMqUtil;
import com.rabbitmq.client.*;
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
        String logsDirect = "logs_direct";
        assert CONNECTION != null;
        try (Channel channel = CONNECTION.createChannel()) {
            channel.basicQos(1);
            channel.exchangeDeclare(logsDirect, "direct");
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, logsDirect, "info");
            channel.queueBind(queueName, logsDirect, "warning");
            channel.queueBind(queueName, logsDirect, "error");
            channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                    String message = new String(body, StandardCharsets.UTF_8);
                    System.out.println(" [x] Received_2 '" + message + "'");
                }
            });
        }
    }

    public static void main(String[] args) {
        Consumer2.receiveMessage();
    }
}
