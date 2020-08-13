package com.test.demo.example.ioc;

public class OrderDao {
    public void createOrder(User currentLoginUser) {
        System.out.println("User " + currentLoginUser.getName() + " creates an order!");
    }
}
