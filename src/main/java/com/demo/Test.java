package com.demo;

import com.demo.rabbitmq.Consumer;
import com.demo.rabbitmq.Provider;
import com.demo.rabbitmq.RabbitmqConfig;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Test {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitmqConfig.getRabbitConnection();
        Provider provider = new Provider(connection);
        provider.sendMessage("hello rabbitmq");

        Consumer consumer = new Consumer(connection);
        consumer.receiveMessage();
        connection.close();
    }
}
