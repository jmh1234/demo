package com.demo.example.ioc;

/**
 * Created with IntelliJ IDEA.
 * OrderDao
 *
 * @author Ji MingHao
 * @since 2022-04-08 11:35
 */
public class OrderDao {
    public void createOrder(User currentLoginUser) {
        System.out.println("User " + currentLoginUser.getName() + " creates an order!");
    }
}
