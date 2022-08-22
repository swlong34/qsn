package com.shwlong.qsn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shwlong.qsn.dao.QuestionMapper;
import com.shwlong.qsn.entity.QuestionEntity;
import com.shwlong.qsn.entity.vo.AddQuestionVo;
import com.shwlong.qsn.service.QuestionService;
import com.shwlong.qsn.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper qsMapper;

    @Override
    public String addOneQuestion(QuestionEntity qs) {
        qsMapper.insert(qs);
        return qs.getId();
    }

    @Override
    public int updateQuestionById(QuestionEntity qs) {
        return qsMapper.updateById(qs);
    }

    @Override
    public int deleteQuestionById(String qsId) {
        return qsMapper.deleteById(qsId);
    }

    @Override
    public List<QuestionEntity> getQuestionListByPaperId(String paperId) {
        QueryWrapper<QuestionEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("paper_id", paperId);
        return qsMapper.selectList(wrapper);
    }

    @Override
    public AddQuestionVo getQuestionInfoByPaperIdAndIndex(String paperId, int i) {
        // 获取题目列表
        List<QuestionEntity> qsList = getQuestionListByPaperId(paperId);

        // 将题目列表按qs_order排序
        qsList.sort(Comparator.comparingInt(QuestionEntity::getQsOrder));

        AddQuestionVo qsVo = new AddQuestionVo();
        qsVo.setQs_title(qsList.get(i).getQsTitle());
        qsVo.setRequired(qsList.get(i).getRequired());
        qsVo.setQs_option(StringUtils.String2List(qsList.get(i).getQsOption()));
        return qsVo;
    }

    @Override
    public void delQuestionByPaperId(String paperId) {
        QueryWrapper<QuestionEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("paper_id", paperId);
        qsMapper.delete(wrapper);
    }

    @Override
    public Integer getOrderByQsId(String qsId) {
        QuestionEntity qs = qsMapper.selectById(qsId);
        return qs.getQsOrder();
    }
}
