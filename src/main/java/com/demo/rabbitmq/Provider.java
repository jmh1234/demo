package com.demo.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import lombok.SneakyThrows;

public class Provider {

    private static final Connection connection = RabbitMQConfig.getRabbitConnection();


    @SneakyThrows
    public static void sendMessage(String message) {
        assert connection != null;
        Channel channel = connection.createChannel();
        channel.queueDeclare("hello", true, false, false, null);
        channel.basicPublish("", "hello", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());

        System.out.println(" [x] Sent '" + message + "'");
        RabbitMQConfig.closeConnectionAndChanel(channel, connection);
    }

    public static void main(String[] args) {
        Provider.sendMessage("hello rabbitmq");
    }
}
