package com.demo;

import com.demo.rabbitmq.Provider;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Test {
    public static void main(String[] args) throws IOException, TimeoutException {
        Provider provider = new Provider();
        provider.testMessageSend();
    }
}
