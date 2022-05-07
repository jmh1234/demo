package com.demo.example.ioc;


import com.demo.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * OrderService
 *
 * @author Ji MingHao
 * @since 2022-04-08 11:35
 */
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private UserService userService;

    public void createOrder() {
        orderDao.createOrder(userService.getCurrentLoginUser());
    }
}
