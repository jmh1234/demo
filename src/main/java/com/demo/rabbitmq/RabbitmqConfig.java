package com.demo.rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitmqConfig {

    public static Connection getRabbitConnection() throws IOException, TimeoutException {
        // 设置连接属性
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("146.56.220.117");
        connectionFactory.setVirtualHost("/jimh");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("jimh");
        connectionFactory.setPassword("jimh");
        // 获取连接对象
        return connectionFactory.newConnection();
    }
}
