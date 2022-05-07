package com.demo.rabbitmq.pointtopoint;

import com.demo.rabbitmq.RabbitMqUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
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

            // 参数2：是否持久化数据 参数3：是否独占队列 参数4：消费完是否删除队列
            channel.queueDeclare("hello", true, false, false, null);
            for (int i = 1; i <= 20; i++) {
                channel.basicPublish("", "hello", MessageProperties.PERSISTENT_TEXT_PLAIN, (message + i).getBytes());
                System.out.println(" [x] Sent '" + message + i + "'");
            }
            RabbitMqUtil.closeConnectionAndChanel(channel, CONNECTION);
        }
    }

    public static void main(String[] args) {
        sendMessage("hello rabbitmq");
    }
}
