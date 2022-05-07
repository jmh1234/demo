package com.demo.service;

import com.demo.entity.Pagination;
import com.demo.entity.User;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * 用户Service
 *
 * @author Ji MingHao
 * @since 2022-05-07 10:21
 */
public interface UserService {

    /**
     * 通过id获取用户信息
     *
     * @param user     user实体类
     * @param offset   偏移量
     * @param pageSize 每页数量
     * @return 查询到的数据
     */
    Pagination<User> getUserById(User user, int offset, int pageSize);

    /**
     * 新增用户数据
     *
     * @param users 用户数据
     */
    void addUserInfo(List<User> users);
}
