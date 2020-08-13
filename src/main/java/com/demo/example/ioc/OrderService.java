package com.demo.example.ioc;


import com.demo.annotation.Autowired;

public class OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private UserService userService;

    public void createOrder() {
        orderDao.createOrder(userService.getCurrentLoginUser());
    }
}
