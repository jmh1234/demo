package com.demo.rabbitmq.fanout;

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
            channel.exchangeDeclare("logs", "fanout");
            channel.basicPublish("logs", "", null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
            RabbitMqUtil.closeConnectionAndChanel(channel, CONNECTION);
        }
    }

    public static void main(String[] args) {
        Provider.sendMessage("fanout rabbitmq");
    }
}
