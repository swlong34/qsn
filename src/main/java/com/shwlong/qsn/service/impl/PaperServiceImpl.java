package com.shwlong.qsn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shwlong.qsn.dao.AnswerInfoMapper;
import com.shwlong.qsn.dao.PaperMapper;
import com.shwlong.qsn.entity.AnswerEntity;
import com.shwlong.qsn.entity.AnswerInfoEntity;
import com.shwlong.qsn.entity.PaperEntity;
import com.shwlong.qsn.entity.QuestionEntity;
import com.shwlong.qsn.entity.vo.*;
import com.shwlong.qsn.exception.QsnException;
import com.shwlong.qsn.service.AnswerService;
import com.shwlong.qsn.service.PaperService;
import com.shwlong.qsn.service.QuestionService;
import com.shwlong.qsn.util.*;
import net.sf.json.JSONArray;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class PaperServiceImpl implements PaperService {

    @Autowired
    private PaperMapper paperMapper;

    @Autowired
    private AnswerInfoMapper ansiMapper;

    @Autowired
    private QuestionService qsService;

    @Autowired
    private AnswerService ansService;

    @Override
    public void savePaper(int userId, AddPaperVo paperVo) {

        // 1.取出问卷数据复制到Paper实体中
        PaperEntity paper = new PaperEntity();
        paper.setUserId(userId);
        paper.setPaperTitle(paperVo.getTitle());
        paper.setPaperFooter(paperVo.getFooter());
        paper.setPaperStatus(Constant.NO_PUBLISH);
        paper.setCreateTime(new Date());
        paper.setStartTime(DateTimeUtils.str2Date(paperVo.getStart_time()));
        paper.setEndTime(DateTimeUtils.str2Date(paperVo.getEnd_time()));

        // 2.保存问卷，获取到问卷id
        paperMapper.insert(paper);
        String paperId = paper.getId();

        // 3.将题目列表按照qs_order字段排序
        List<AddQuestionVo> qsList = paperVo.getContent();
        qsList.sort(Comparator.comparingInt(AddQuestionVo::getQs_order));

        // 4.将问题数据复制到问题实体，根据问卷id保存问卷
        for(AddQuestionVo qsVo : qsList) {
            QuestionEntity qs = new QuestionEntity();
            qs.setPaperId(paperId);
            qs.setQsTitle(qsVo.getQs_title());
            qs.setQsType(Integer.parseInt(qsVo.getQs_type()));
            qs.setQsOption(JSONArray.fromObject(qsVo.getQs_option()).toString());
            qs.setQsAnswer("");
            qs.setQsOrder(qsVo.getQs_order());
            qs.setRequired(qsVo.getRequired());
            qsService.addOneQuestion(qs);
        }

    }

    @Override
    public void editPaper(int userId, AddPaperVo paperVo) {
        // 1.将问卷信息复制到问卷实体
        PaperEntity paper = new PaperEntity();
        String paperId = paperVo.getPaper_id();
        paper.setId(paperId);
        paper.setUserId(userId);
        paper.setPaperTitle(paperVo.getTitle());
        paper.setPaperFooter(paperVo.getFooter());
        paper.setPaperStatus(paperVo.getStatus());
        paper.setStartTime(DateTimeUtils.str2Date(paperVo.getStart_time()));
        paper.setEndTime(DateTimeUtils.str2Date(paperVo.getEnd_time()));

        // 2.根据问卷id更新问卷信息
        paperMapper.updatePaperById(paper);

        // 3.获取前端的题目信息
        List<AddQuestionVo> qsVoList = paperVo.getContent();
        List<QuestionEntity> qsList = qsService.getQuestionListByPaperId(paperId);
        List<String> updateQsId = new ArrayList<>();

        // 4.若题目id存在则，更新题目；否则，添加题目
        for(AddQuestionVo qsVo : qsVoList) {
            QuestionEntity qs = new QuestionEntity();
            qs.setId(qsVo.getQs_id());
            qs.setPaperId(paperId);
            qs.setQsTitle(qsVo.getQs_title());
            qs.setQsType(Integer.parseInt(qsVo.getQs_type()));
            qs.setQsOption(JSONArray.fromObject(qsVo.getQs_option()).toString());
            qs.setQsAnswer("");
            qs.setQsOrder(qsVo.getQs_order());
            qs.setRequired(qsVo.getRequired());
            // 若id为空，添加题目；否则，更新题目
            if(org.apache.commons.lang3.StringUtils.isBlank(qs.getId()))
                qsService.addOneQuestion(qs);
            else {
                updateQsId.add(qs.getId());
                qsService.updateQuestionById(qs);
            }
        }

        // 5.若此次前端有未传来的题目信息，判定为删除
        for(QuestionEntity qs : qsList)
            if(!updateQsId.contains(qs.getId()))
                qsService.deleteQuestionById(qs.getId());
    }

    @Override
    public String addOnePaper(PaperEntity paper) {
        paperMapper.insert(paper);
        return paper.getId();
    }

    @Override
    public List<PaperEntity> getPaperList() {
        return paperMapper.selectList(null);
    }

    @Override
    public List<GetPaperVo> getPaperListByUserId(int userId) {
        QueryWrapper<PaperEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        List<PaperEntity> list = paperMapper.selectList(wrapper);
        List<GetPaperVo> voList = new ArrayList<>();
        for(PaperEntity paper : list) {
            GetPaperVo paperVo = new GetPaperVo();
            paperVo.setId(paper.getId());
            paperVo.setPaperTitle(paper.getPaperTitle());
            paperVo.setPaperStatus(paper.getPaperStatus());
            paperVo.setCreateTime(paper.getCreateTime());
            paperVo.setStartTime(paper.getStartTime());
            paperVo.setEndTime(paper.getEndTime());
            paperVo.setAnsPapers(paper.getSubmitPerson());
            voList.add(paperVo);
        }
        return voList;
    }

    @Override
    public List<GetPaperVo> getPaperListByUserIdAndTitle(int userId, String text) {
        QueryWrapper<PaperEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId).like("paper_title", text);
        List<PaperEntity> list = paperMapper.selectList(wrapper);
        List<GetPaperVo> voList = new ArrayList<>();
        for(PaperEntity paper : list) {
            GetPaperVo paperVo = new GetPaperVo();
            paperVo.setId(paper.getId());
            paperVo.setPaperTitle(paper.getPaperTitle());
            paperVo.setPaperStatus(paper.getPaperStatus());
            paperVo.setCreateTime(paper.getCreateTime());
            paperVo.setStartTime(paper.getStartTime());
            paperVo.setEndTime(paper.getEndTime());
            paperVo.setAnsPapers(paper.getSubmitPerson());
            voList.add(paperVo);
        }
        return voList;
    }

    @Override
    public AddPaperVo getPaperInfoById(String paperId) {
        // 1.根据问卷id查询问卷
        PaperEntity paper = paperMapper.selectById(paperId);

        // 2.将问卷信息保存到返回对象
        AddPaperVo paperVo = new AddPaperVo();
        paperVo.setPaper_id(paper.getId());
        paperVo.setTitle(paper.getPaperTitle());
        paperVo.setFooter(paper.getPaperFooter());
        paperVo.setStart_time(DateTimeUtils.date2FormatStr(paper.getStartTime()));
        paperVo.setEnd_time(DateTimeUtils.date2FormatStr(paper.getEndTime()));
        paperVo.setStatus(paper.getPaperStatus());

        // 3.根据问卷id查询该问卷的题目信息
        List<QuestionEntity> qsList = qsService.getQuestionListByPaperId(paperId);

        // 4.将题目列表按qs_order字段排序
        qsList.sort(Comparator.comparingInt(QuestionEntity::getQsOrder));

        // 5.将题目信息保存到返回对象
        paperVo.setQs_count(qsList.size());
        List<AddQuestionVo> qsVos = new ArrayList<>();
        for(QuestionEntity qs : qsList) {
            AddQuestionVo qsVo = new AddQuestionVo();
            qsVo.setQs_id(qs.getId());
            qsVo.setQs_order(qs.getQsOrder());
            qsVo.setQs_number(qs.getQsOrder());
            qsVo.setQs_title(qs.getQsTitle());
            qsVo.setQs_type(qs.getQsType()+"");
            qsVo.setRequired(qs.getRequired());
            qsVo.setQs_option(StringUtils.String2List(qs.getQsOption()));
            qsVos.add(qsVo);
        }
        paperVo.setContent(qsVos);

        return paperVo;
    }

    @Override
    public void updateStatusByPaperId(String paperId, Integer isPublished) {
        paperMapper.updatePaperStatusById(paperId, isPublished);
    }

    @Override
    public List<GetPaperVo> getPaperListByUserIdAndStatus(int userId, Integer status) {
        QueryWrapper<PaperEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("paper_status",status);
        List<PaperEntity> list = paperMapper.selectList(wrapper);
        List<GetPaperVo> voList = new ArrayList<>();
        for(PaperEntity paper : list) {
            GetPaperVo paperVo = new GetPaperVo();
            paperVo.setId(paper.getId());
            paperVo.setPaperTitle(paper.getPaperTitle());
            paperVo.setPaperStatus(paper.getPaperStatus());
            paperVo.setCreateTime(paper.getCreateTime());
            paperVo.setStartTime(paper.getStartTime());
            paperVo.setEndTime(paper.getEndTime());
            paperVo.setAnsPapers(paper.getSubmitPerson());
            voList.add(paperVo);
        }
        return voList;
    }

    @Override
    public void saveAnswerByPaperId(String paperId, List<AddAnswerVo> ansVos) {
        AnswerInfoEntity ansiEntity = new AnswerInfoEntity();
        ansiEntity.setSubmitTime(new Date());
        ansiEntity.setPaperId(paperId);
        ansiMapper.insert(ansiEntity);
        Integer saveId = ansiEntity.getId();

        for(AddAnswerVo ansVo : ansVos) {
            AnswerEntity ans = new AnswerEntity();
            ans.setPaperId(paperId);
            ans.setQsId(ansVo.getQs_id());
            ans.setQsType(ansVo.getQs_type());
            ans.setAnswerOption(ansVo.getQs_option());
            ans.setSaveId(saveId);
            ansService.addOneAnswer(ans);
        }

        // 同时更新paper表中的提交人数
        paperMapper.updateSubmitPersonByPaperId(paperId);
    }

    @Override
    public PaperEntity getPaperById(String paperId) {
        return paperMapper.selectById(paperId);
    }

    @Override
    public void delPaperInfoAndData(String paperId) {
        // 1.根据paperId删除答卷数据
        ansService.delAnswerByPaperId(paperId);

        // 2.根据paperId删除题目信息
        qsService.delQuestionByPaperId(paperId);

        // 3.根据paperId删除问卷信息
        paperMapper.deleteById(paperId);

    }

    @Override
    public List<PaperEntity> getPaperListByStatus(Integer isPublished) {
        QueryWrapper<PaperEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("paper_status", isPublished);
        List<PaperEntity> list = paperMapper.selectList(wrapper);
        return list;
    }

    @Override
    public XSSFWorkbook downloadExcel(String paperId) throws IOException {
        // 1.根据paperId获取题目信息
        List<QuestionEntity> qsList = qsService.getQuestionListByPaperId(paperId);
        List<SaveQsTitleVo> qsTitleVos = new ArrayList<>();
        qsList.sort(Comparator.comparingInt(QuestionEntity::getQsOrder));
        for(QuestionEntity qs : qsList) {
            SaveQsTitleVo qsTitleVo = new SaveQsTitleVo();
            qsTitleVo.setQsOrder(qs.getQsOrder());
            qsTitleVo.setQsType(qs.getQsType());
            qsTitleVo.setQsTitle(qs.getQsTitle());
            qsTitleVos.add(qsTitleVo);
        }

        // 2.根据paperId获取答卷信息
        List<SaveToExcelVo> excelVos = new ArrayList<>();
        List<AnswerInfoEntity> ansInfoList = ansService.getAnswerInfoList(paperId);
        int i = 1;
        for(AnswerInfoEntity ansInfo : ansInfoList) {
            SaveToExcelVo excelVo = new SaveToExcelVo();
            excelVo.setIndex(i++);
            excelVo.setSubmitTime(ansInfo.getSubmitTime());
            List<AnswerEntity> ansList = ansService.getAnswerListByPaperIdAndSaveId(paperId, ansInfo.getId());
            List<SaveAnswerVo> answerVos = new ArrayList<>();
            for(AnswerEntity ans : ansList) {
                SaveAnswerVo answerVo = new SaveAnswerVo();
                answerVo.setQsOrder(qsService.getOrderByQsId(ans.getQsId()));
                answerVo.setQsType(ans.getQsType());
                answerVo.setOptions(ans.getAnswerOption());
                answerVos.add(answerVo);
            }
            excelVo.setAns(answerVos);
            excelVos.add(excelVo);
        }

        // 3.写入到excel文件并保存
        return FileInOut.ExcelExport(qsTitleVos, excelVos);

    }

    @Override
    public XWPFDocument downloadWord(String paperId) throws IOException {
        AddPaperVo paperVo = getPaperInfoById(paperId);
        return FileInOut.WordExport(paperVo);
    }

    @Override
    public void updateEndTimeByPaperId(String paperId, Date date) {
        paperMapper.updatePaperEndTimeById(paperId, date);
    }

    @Override
    public List<QsAnalyseVo> getPaperAnalyseInfoByPaperId(String paperId) {
        List<QsAnalyseVo> list = new ArrayList<>();
        List<QuestionEntity> qsList = qsService.getQuestionListByPaperId(paperId);

        for(QuestionEntity qs : qsList) {

            // 获取总的答卷数
            Integer total = ansService.getAnswerCountByPaperIdAndQsId(paperId, qs.getId());

            if(total == 0)
                throw new QsnException(QsnEnum.PAPER_HAVE_NO_ANSWER.getMsg());

            QsAnalyseVo qsVo = new QsAnalyseVo();
            List<AnswerEntity> ansList = ansService.getAnswerByPaperIdAndQsId(paperId, qs.getId());

            qsVo.setSubmit_person(total);
            qsVo.setQs_order(qs.getQsOrder());
            qsVo.setQs_title(qs.getQsTitle());
            qsVo.setQs_type(qs.getQsType()+"");

            // 选择题
            if(qs.getQsType() / 10 == 1) {
                List<String> options = StringUtils.String2List(qs.getQsOption());

                // 创建并初始化map
                Map<String, Integer> map = new LinkedHashMap<>();
                for(String opt : options) map.put(opt, 0);

                // 统计每个选项选择的人数
                for(AnswerEntity ans: ansList) {
                    String ansOpt = ans.getAnswerOption();
                    for(String opt : options)
                        if(ansOpt.contains(opt))
                            map.put(opt, map.get(opt) + 1);
                }

                // 设置选项统计字段
                List<Map<String, String>> qsOption = new ArrayList<>();
                map.forEach((k, v) -> {
                    Map<String, String> mp = new LinkedHashMap<>();
                    mp.put("option", k);
                    mp.put("subtotal", v+"");
                    mp.put("percentage", String.format("%.0f", (v / Math.ceil(total)) * 100));
                    qsOption.add(mp);
                });
                qsVo.setQs_option(qsOption);
            }
            // 填空题
            else {
                List<Map<String, String>> qsOption = new ArrayList<>();
                for(AnswerEntity ans: ansList) {
                    Map<String, String> mp = new LinkedHashMap<>();
                    mp.put("option", ans.getAnswerOption());
                    qsOption.add(mp);
                }
                qsVo.setQs_option(qsOption);
            }
            list.add(qsVo);
        }
        list.sort(Comparator.comparingInt(QsAnalyseVo::getQs_order));
        return list;
    }
}
