package com.demo.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.SneakyThrows;

/**
 * Created with IntelliJ IDEA.
 * RabbitMqUtil
 *
 * @author Ji MingHao
 * @since 2022-05-07 10:21
 */
public class RabbitMqUtil {

    private RabbitMqUtil() {

    }

    private static final ConnectionFactory CONNECTION_FACTORY = new ConnectionFactory();

    static {
        CONNECTION_FACTORY.setHost("146.56.220.117");
        CONNECTION_FACTORY.setVirtualHost("/jimh");
        CONNECTION_FACTORY.setPort(5672);
        CONNECTION_FACTORY.setUsername("jimh");
        CONNECTION_FACTORY.setPassword("jimh");
    }

    @SneakyThrows
    public static Connection getRabbitConnection() {
        return CONNECTION_FACTORY.newConnection();
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
