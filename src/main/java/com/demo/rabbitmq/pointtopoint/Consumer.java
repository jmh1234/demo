package com.demo.rabbitmq.pointtopoint;

import com.demo.rabbitmq.RabbitMqUtil;
import com.demo.util.LoggerUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * Created with IntelliJ IDEA.
 * Consumer
 *
 * @author Ji MingHao
 * @since 2022-05-07 10:21
 */
public class Consumer {

    private static final Connection CONNECTION = RabbitMqUtil.getRabbitConnection();

    @SneakyThrows(IOException.class)
    public static void receiveMessage() {
        assert CONNECTION != null;
        try (Channel channel = CONNECTION.createChannel()) {
            // 每次只能消费一个消息
            channel.basicQos(1);
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
        } catch (TimeoutException e) {
            LoggerUtil.handleException(e);
        }
    }

    public static void main(String[] args) {
        receiveMessage();
    }
}
