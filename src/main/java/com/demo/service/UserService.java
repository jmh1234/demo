package com.demo.service;

import com.demo.domain.User;
import com.demo.util.Pagination;

import java.util.List;

public interface UserService {

    Pagination<User> getUserById(User user, int pageNum, int pageSize) throws Exception;

    void addUserInfo(List<User> users);
}
