package com.shwlong.qsn.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shwlong.qsn.entity.PaperEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

@Mapper
public interface PaperMapper extends BaseMapper<PaperEntity> {

    int updatePaperById(PaperEntity paper);

    void updatePaperStatusById(@Param("id") String paperId, @Param("pub") Integer isPublished);

    void updateSubmitPersonByPaperId(@Param("id") String paperId);

    void updatePaperEndTimeById(@Param("id") String paperId, @Param("date") Date date);
}
