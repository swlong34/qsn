package com.shwlong.qsn.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shwlong.qsn.entity.PaperModelEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ModelMapper extends BaseMapper<PaperModelEntity> {
}
