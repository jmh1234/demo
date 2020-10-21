package com.demo.rabbitmq.direct;

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
        channel.exchangeDeclare("logs_direct", "direct");
        String routeKey = "error";
        String actualMessage = message + (" [" + routeKey + "] 发送的信息");
        channel.basicPublish("logs_direct", routeKey, null, actualMessage.getBytes());
        System.out.println(" [x] Sent '" + actualMessage + "'");
        RabbitMQConfig.closeConnectionAndChanel(channel, connection);
    }

    public static void main(String[] args) {
        Provider.sendMessage("这事direct模型发布的基于 route key");
    }
}
