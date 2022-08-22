package com.shwlong.qsn.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shwlong.qsn.entity.QsModelEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ModelQsMapper extends BaseMapper<QsModelEntity> {
}
