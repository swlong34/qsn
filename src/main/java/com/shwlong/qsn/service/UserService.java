package com.shwlong.qsn.service;

import com.shwlong.qsn.entity.UserEntity;

import java.util.List;

public interface UserService {

    /**
     * 获取用户列表
     * @return
     */
    List<UserEntity> getUsers();

    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
    UserEntity getUserByUserName(String username);

    /**
     * 添加一个用户
     */
    int addOneUser(UserEntity userEntity);

    UserEntity getUserByUserNameOrNumber(String account);
}
