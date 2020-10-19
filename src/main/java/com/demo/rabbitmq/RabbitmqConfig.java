package com.demo.rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitmqConfig {

    public static Connection getRabbitConnection() throws IOException, TimeoutException {
        // 设置连接属性
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setVirtualHost("/jimh");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("jimh1234");
        connectionFactory.setPassword("jimh1234");
        // 获取连接对象
        return connectionFactory.newConnection();
    }
}
