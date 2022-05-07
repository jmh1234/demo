package com.demo.service.impl;

import com.demo.dao.UserDao;
import com.demo.entity.Pagination;
import com.demo.entity.User;
import com.demo.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * 用户Service
 *
 * @author Ji MingHao
 * @since 2022-05-07 10:21
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    /**
     * 通过id获取用户信息
     *
     * @param user     user实体类
     * @param offset   偏移量
     * @param pageSize 每页数量
     * @return 查询到的数据
     */
    @Override
    public Pagination<User> getUserById(User user, int offset, int pageSize) {
        PageMethod.startPage(offset, pageSize);
        List<User> usersList = userDao.selectByPrimaryKey(user);
        try (Page<User> users = new Page<>()) {
            users.addAll(usersList);
            int total = (int) users.getTotal();
            int totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
            return Pagination.pageOf(usersList, pageSize, offset, totalPage);
        }
    }

    /**
     * 新增用户数据
     *
     * @param users 用户数据
     */
    @Override
    public void addUserInfo(List<User> users) {
        userDao.addUserInfo(users);
    }
}
