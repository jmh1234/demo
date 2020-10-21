package com.demo.rabbitmq.fanout;

import com.demo.rabbitmq.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.SneakyThrows;

public class Provider {

    private static final Connection connection = RabbitMQConfig.getRabbitConnection();

    @SneakyThrows
    public static void sendMessage(String message) {
        assert connection != null;
        Channel channel = connection.createChannel();
        channel.exchangeDeclare("logs", "fanout");
        channel.basicPublish("logs", "", null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");
        RabbitMQConfig.closeConnectionAndChanel(channel, connection);
    }

    public static void main(String[] args) {
        Provider.sendMessage("fanout rabbitmq");
    }
}
