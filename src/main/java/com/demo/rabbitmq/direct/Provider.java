package com.demo.rabbitmq.direct;

import com.demo.rabbitmq.RabbitMqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.SneakyThrows;

/**
 * Created with IntelliJ IDEA.
 * Provider
 *
 * @author Ji MingHao
 * @since 2022-05-07 10:21
 */
public class Provider {

    private static final Connection CONNECTION = RabbitMqUtil.getRabbitConnection();

    @SneakyThrows
    public static void sendMessage(String message) {
        assert CONNECTION != null;
        try (Channel channel = CONNECTION.createChannel()) {
            channel.exchangeDeclare("logs_direct", "direct");
            String routeKey = "error";
            String actualMessage = message + (" [" + routeKey + "] 发送的信息");
            channel.basicPublish("logs_direct", routeKey, null, actualMessage.getBytes());
            System.out.println(" [x] Sent '" + actualMessage + "'");
            RabbitMqUtil.closeConnectionAndChanel(channel, CONNECTION);
        }
    }

    public static void main(String[] args) {
        Provider.sendMessage("这事direct模型发布的基于 route key");
    }
}
