package com.test.demo.example.ioc;


import com.test.demo.annotation.Autowired;

public class UserService {
    @Autowired
    private UserDao userDao;

    public User getCurrentLoginUser() {
        return userDao.getUserById(1);
    }
}
