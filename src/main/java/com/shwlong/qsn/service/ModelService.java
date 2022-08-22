package com.shwlong.qsn.service;

import com.shwlong.qsn.entity.vo.*;

import java.util.List;

public interface ModelService {

    /**
     * 创建问卷模板
     * @param modelVo
     */
    void createPaperModelByModelVo(CreateModelVo modelVo);

    /**
     * 获取所有问卷模板
     * @return
     */
    List<GetModelVo> getPaperModelList();

    /**
     * 获取所有标签
     * @return
     */
    List<GetTagVo> getTagList();

    /**
     * 获取模板问卷信息
     * @param modelId
     * @return
     */
    AddPaperVo getModelInfoById(String modelId);

    /**
     * 根据模板id和题目顺序获取一个题目
     * @param modelId
     * @param i
     * @return
     */
    AddQuestionVo getQuestionInfoByModelIdAndIndex(String modelId, int i);

    /**
     * 根据查询条件获取问卷模板列表
     * @param smVo
     * @return
     */
    List<GetModelVo> getPaperModelListByCond(SearchModelVo smVo);
}
