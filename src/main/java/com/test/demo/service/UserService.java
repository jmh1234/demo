package com.test.demo.service;

import com.test.demo.domain.User;
import com.test.demo.util.Pagination;

public interface UserService {
    Pagination<User> getUserById(User user, int pageNum, int pageSize) throws Exception;
}
