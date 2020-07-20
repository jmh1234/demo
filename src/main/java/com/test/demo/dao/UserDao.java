package com.test.demo.dao;

import com.test.demo.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {

    User selectByPrimaryKey(String paramsList);
}
