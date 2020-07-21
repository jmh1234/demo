package com.test.demo.service.Impl;

import com.test.demo.dao.UserDao;
import com.test.demo.domain.User;
import com.test.demo.service.UserService;
import com.test.demo.util.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public User getUserById(String paramsList) {
        User user = null;
        try {
            user = userDao.selectByPrimaryKey(paramsList);
        } catch (Exception e) {
            logger.error(LoggerUtil.handleException(e));
        }
        return user;
    }
}
