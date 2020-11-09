package com.demo.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.demo.dao.UserDao;
import com.demo.entity.User;
import com.demo.service.UserService;
import com.demo.entity.Pagination;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public Pagination<User> getUserById(User user, int offset, int pageSize) {
        PageHelper.startPage(offset, pageSize);
        List<User> usersList = userDao.selectByPrimaryKey(user);
        int total = (int) ((Page) usersList).getTotal();
        int totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        return Pagination.pageOf(usersList, pageSize, offset, totalPage);
    }

    @Override
    public void addUserInfo(List<User> users) {
        userDao.addUserInfo(users);
    }
}
