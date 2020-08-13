package com.demo;

import com.demo.example.ioc.MyIoCContainer;
import com.demo.example.ioc.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Properties;

public class MyIoCContainerTest {


    @Test
    public void beanTest() {
        try {
            Properties properties = new Properties();
            properties.load(MyIoCContainer.class.getResourceAsStream("/ioc.properties"));
            MyIoCContainer container = new MyIoCContainer(properties);
            container.start();
            OrderService orderService = (OrderService) container.getBean("orderService");
            orderService.createOrder();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void singletonBeanTest() {
        try {
            Properties properties = new Properties();
            properties.load(MyIoCContainer.class.getResourceAsStream("/ioc.properties"));
            MyIoCContainer container = new MyIoCContainer(properties);
            container.start();
            Assertions.assertSame(container.getBean("orderService"), container.getBean("orderService"));
            Assertions.assertSame(container.getBean("userService"), container.getBean("userService"));
            Assertions.assertSame(container.getBean("orderDao"), container.getBean("orderDao"));
            Assertions.assertSame(container.getBean("userDao"), container.getBean("userDao"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
