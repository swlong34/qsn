package com.shwlong.qsn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shwlong.qsn.dao.AnswerInfoMapper;
import com.shwlong.qsn.dao.AnswerMapper;
import com.shwlong.qsn.entity.AnswerEntity;
import com.shwlong.qsn.entity.AnswerInfoEntity;
import com.shwlong.qsn.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private AnswerInfoMapper answerInfoMapper;

    @Override
    public String addOneAnswer(AnswerEntity ans) {
        answerMapper.insert(ans);
        return ans.getId();
    }

    @Override
    public List<AnswerEntity> getAnswerByPaperIdAndQsId(String paperId, String qsId) {
        QueryWrapper<AnswerEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("paper_id", paperId).eq("qs_id", qsId);
        return answerMapper.selectList(wrapper);
    }

    @Override
    public Integer getAnswerCountByPaperIdAndQsId(String paperId, String qsId) {
        return answerMapper.getAnswerCountByPaperIdAndQsId(paperId, qsId);
    }


    @Override
    public void delAnswerByPaperId(String paperId) {
        QueryWrapper<AnswerEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("paper_id", paperId);
        answerMapper.delete(wrapper);
    }

    @Override
    public List<AnswerInfoEntity> getAnswerInfoList(String paperId) {
        QueryWrapper<AnswerInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("paper_id", paperId);
        return answerInfoMapper.selectList(wrapper);
    }

    @Override
    public List<AnswerEntity> getAnswerListByPaperIdAndSaveId(String paperId, Integer id) {
        QueryWrapper<AnswerEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("paper_id", paperId).eq("save_id", id);
        return answerMapper.selectList(wrapper);
    }
}
