package com.demo.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.SneakyThrows;

public class RabbitMQUtil {
    private static final ConnectionFactory connectionFactory = new ConnectionFactory();

    static {
        connectionFactory.setHost("146.56.220.117");
        connectionFactory.setVirtualHost("/jimh");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("jimh");
        connectionFactory.setPassword("jimh");
    }

    @SneakyThrows
    public static Connection getRabbitConnection() {
        return connectionFactory.newConnection();
    }

    @SneakyThrows
    public static void closeConnectionAndChanel(Channel channel, Connection conn) {
        if (channel != null) {
            channel.close();
        }
        if (conn != null) {
            conn.close();
        }
    }
}
