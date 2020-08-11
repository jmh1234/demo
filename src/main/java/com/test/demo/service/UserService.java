package com.test.demo.service;

import com.test.demo.domain.User;
import com.test.demo.util.Pagination;

import java.util.List;

public interface UserService {

    Pagination<User> getUserById(User user, int pageNum, int pageSize) throws Exception;

    void addUserInfo(List<User> users);
}
