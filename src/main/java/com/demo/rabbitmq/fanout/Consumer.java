package com.demo.rabbitmq.fanout;

import com.demo.rabbitmq.RabbitMqUtil;
import com.demo.util.LoggerUtil;
import com.rabbitmq.client.*;
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
        } catch (TimeoutException e) {
            LoggerUtil.handleException(e);
        }
    }

    public static void main(String[] args) {
        Consumer.receiveMessage();
    }
}
