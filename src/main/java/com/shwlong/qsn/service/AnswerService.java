package com.shwlong.qsn.service;

import com.shwlong.qsn.entity.AnswerEntity;
import com.shwlong.qsn.entity.AnswerInfoEntity;

import java.util.List;

public interface AnswerService {

    String addOneAnswer(AnswerEntity ans);

    /**
     * 根据问卷id和问题id查询答案
     * @param paperId
     * @param qsId
     * @return
     */
    List<AnswerEntity> getAnswerByPaperIdAndQsId(String paperId, String qsId);

    /**
     * 根据问卷id和题目id获取答卷人数
     * @param paperId
     * @param qsId
     * @return
     */
    Integer getAnswerCountByPaperIdAndQsId(String paperId, String qsId);

    void delAnswerByPaperId(String paperId);

    List<AnswerInfoEntity> getAnswerInfoList(String paperId);

    List<AnswerEntity> getAnswerListByPaperIdAndSaveId(String paperId, Integer id);
}
