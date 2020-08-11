package com.test.demo.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.test.demo.dao.UserDao;
import com.test.demo.domain.User;
import com.test.demo.service.UserService;
import com.test.demo.util.Pagination;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    public Pagination<User> getUserById(User user, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> usersList = userDao.selectByPrimaryKey(user);
        int total = (int) ((Page) usersList).getTotal();
        Integer totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        return Pagination.pageOf(usersList, pageSize, pageNum, totalPage);
    }

    @Override
    public void addUserInfo(List<User> users) {
        userDao.addUserInfo(users);
    }
}
