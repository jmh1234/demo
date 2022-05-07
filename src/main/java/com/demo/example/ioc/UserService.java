package com.demo.example.ioc;


import com.demo.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * UserService
 *
 * @author Ji MingHao
 * @since 2022-04-08 11:35
 */
public class UserService {

    @Autowired
    private UserDao userDao;

    public User getCurrentLoginUser() {
        return userDao.getUserById(1);
    }
}
