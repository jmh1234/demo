package com.demo.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Provider {

    private final Connection connection;

    public Provider() throws IOException, TimeoutException {
        this.connection = RabbitmqConfig.getRabbitConnection();
    }

    public void testMessageSend() throws IOException, TimeoutException {
        // 创建连接通道
        Channel channel = connection.createChannel();
        // 通过通道绑定对应的消息队列
        channel.queueDeclare("hello", false, false, false, null);
        channel.basicPublish("", "hello", null, "hello rabbitmq".getBytes());

        channel.close();
        connection.close();
    }
}
