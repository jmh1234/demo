package com.demo.service;

import com.demo.entity.User;
import com.demo.entity.Pagination;

import java.util.List;

public interface UserService {

    Pagination<User> getUserById(User user, int offset, int pageSize) throws Exception;

    void addUserInfo(List<User> users);
}
