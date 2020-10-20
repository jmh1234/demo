package com.demo.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

public class Provider {

    private final Connection connection;

    public Provider(Connection connection) {
        this.connection = connection;
    }

    public void sendMessage(String message) throws IOException {
        // 创建连接通道
        Channel channel = connection.createChannel();
        // 通过通道绑定对应的消息队列
        channel.queueDeclare("hello", false, false, false, null);
        channel.basicPublish("", "hello", null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");
    }
}
