package com.demo.example.ioc;

/**
 * Created with IntelliJ IDEA.
 * UserDao
 *
 * @author Ji MingHao
 * @since 2022-04-08 11:35
 */
public class UserDao {
    public User getUserById(Integer id) {
        return new User(id, "user" + id);
    }
}
