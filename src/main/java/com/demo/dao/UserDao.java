package com.demo.dao;

import com.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * 用户Dao
 *
 * @author Ji MingHao
 * @since 2022-05-07 10:21
 */
@Mapper
public interface UserDao {

    /**
     * 通过主键查找数据
     *
     * @param user 用户实体类
     * @return 查询到的数据
     */
    List<User> selectByPrimaryKey(User user);

    /**
     * 新增用户数据
     *
     * @param users 用户数据
     */
    void addUserInfo(List<User> users);
}
