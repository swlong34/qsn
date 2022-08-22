package com.shwlong.qsn.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shwlong.qsn.entity.AnswerEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AnswerMapper extends BaseMapper<AnswerEntity> {

    Integer getAnswerCountByPaperIdAndQsId(@Param("paperId") String paperId, @Param("qsId") String qsId);

}
