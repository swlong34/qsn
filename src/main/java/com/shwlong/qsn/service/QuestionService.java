package com.shwlong.qsn.service;

import com.shwlong.qsn.entity.QuestionEntity;
import com.shwlong.qsn.entity.vo.AddQuestionVo;

import java.util.List;

public interface QuestionService {

    /**
     * 添加一个问题
     * @param qs
     * @return 返回问题id
     */
    String addOneQuestion(QuestionEntity qs);

    /**
     * 根据题目id更新题目
     * @param qs
     * @return
     */
    int updateQuestionById(QuestionEntity qs);

    /**
     * 根据id删除题目信息
     * @param qsId
     * @return
     */
    int deleteQuestionById(String qsId);

    /**
     * 根据问卷id获取题目列表
     * @param paperId
     * @return
     */
    List<QuestionEntity> getQuestionListByPaperId(String paperId);

    /**
     * 根据问卷id和题目顺序获取一个题目
     * @param paperId
     * @param i
     * @return
     */
    AddQuestionVo getQuestionInfoByPaperIdAndIndex(String paperId, int i);

    void delQuestionByPaperId(String paperId);

    Integer getOrderByQsId(String qsId);
}
