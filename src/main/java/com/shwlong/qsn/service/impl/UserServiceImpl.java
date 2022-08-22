package com.shwlong.qsn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shwlong.qsn.dao.UserMapper;
import com.shwlong.qsn.entity.UserEntity;
import com.shwlong.qsn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<UserEntity> getUsers() {
        return userMapper.selectList(null);
    }

    @Override
    public UserEntity getUserByUserName(String username) {
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public int addOneUser(UserEntity userEntity) {
        userMapper.insert(userEntity);
        return userEntity.getId();
    }

    @Override
    public UserEntity getUserByUserNameOrNumber(String account) {
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("username",account).or().eq("number", account);
        return userMapper.selectOne(wrapper);
    }

}
