package com.shwlong.qsn.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shwlong.qsn.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}
