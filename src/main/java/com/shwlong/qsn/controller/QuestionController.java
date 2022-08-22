package com.shwlong.qsn.controller;

import com.shwlong.qsn.entity.QuestionEntity;
import com.shwlong.qsn.entity.vo.AddQuestionVo;
import com.shwlong.qsn.service.QuestionService;
import com.shwlong.qsn.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/qs")
public class QuestionController {

    @Autowired
    private QuestionService qsService;

    /**
     * 根据问卷id和题目顺序获取题目信息
     *
     * @return
     */
    @RequestMapping("/info/{id}/{index}")
    public R getQuestionInfo(@PathVariable("id") String paperId, @PathVariable("index") int index) {
        AddQuestionVo qsVo = qsService.getQuestionInfoByPaperIdAndIndex(paperId, index - 1);
        return R.ok().put("data", qsVo);
    }

}
