package com.demo.rabbitmq.pointToPoint;

import com.demo.rabbitmq.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import lombok.SneakyThrows;

public class Provider {

    private static final Connection connection = RabbitMQUtil.getRabbitConnection();

    @SneakyThrows
    public static void sendMessage(String message) {
        assert connection != null;
        Channel channel = connection.createChannel();
        channel.exchangeDeclare("logs", "fanout");

        // 参数2：是否持久化数据 参数3：是否独占队列 参数4：消费完是否删除队列
        channel.queueDeclare("hello", true, false, false, null);
        for (int i = 1; i <= 20; i++) {
            channel.basicPublish("", "hello", MessageProperties.PERSISTENT_TEXT_PLAIN, (message + i).getBytes());
            System.out.println(" [x] Sent '" + message + i + "'");
        }
        RabbitMQUtil.closeConnectionAndChanel(channel, connection);
    }

    public static void main(String[] args) {
        sendMessage("hello rabbitmq");
    }
}
