package com.demo.dao;

import com.demo.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {
    List<User> selectByPrimaryKey(User user);

    void addUserInfo(List<User> users);
}
