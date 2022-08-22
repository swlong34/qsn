package com.shwlong.qsn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shwlong.qsn.dao.ModelMapper;
import com.shwlong.qsn.dao.ModelQsMapper;
import com.shwlong.qsn.dao.ModelTagMapper;
import com.shwlong.qsn.entity.*;
import com.shwlong.qsn.entity.vo.*;
import com.shwlong.qsn.service.ModelService;
import com.shwlong.qsn.service.PaperService;
import com.shwlong.qsn.service.QuestionService;
import com.shwlong.qsn.util.DateTimeUtils;
import com.shwlong.qsn.util.StringUtils;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class ModelServiceImpl implements ModelService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ModelQsMapper mqMapper;

    @Autowired
    private ModelTagMapper mtMapper;

    @Autowired
    private PaperService paperService;

    @Autowired
    private QuestionService qsService;

    @Override
    public void createPaperModelByModelVo(CreateModelVo modelVo) {
        PaperModelEntity pm = new PaperModelEntity();

        // 1.根据问卷Id获取问卷信息，复制到问卷模板表
        String paperId = modelVo.getPaperId();
        PaperEntity paper = paperService.getPaperById(paperId);
        pm.setCreateTime(new Date());
        pm.setTitle(paper.getPaperTitle());
        pm.setFooter(paper.getPaperFooter());
        pm.setFromUser(modelVo.getUser());
        pm.setDetail(modelVo.getDesc());
        pm.setTags(JSONArray.fromObject(modelVo.getTags()).toString());
        modelMapper.insert(pm);
        String pmId = pm.getId();

        // 2.将问题列表复制到问卷模板对应的题目表
        List<QuestionEntity> qsList = qsService.getQuestionListByPaperId(paperId);
        qsList.sort(Comparator.comparingInt(QuestionEntity::getQsOrder));

        for(QuestionEntity qs : qsList) {
            QsModelEntity qm = new QsModelEntity();
            qm.setMpId(pmId);
            qm.setQsOrder(qs.getQsOrder());
            qm.setQsType(qs.getQsType());
            qm.setQsTitle(qs.getQsTitle());
            qm.setQsOption(qs.getQsOption());
            mqMapper.insert(qm);
        }

        // 3.将问卷标签更新到标签表中
        List<String> tags = modelVo.getTags();
        List<TagModelEntity> tmList = mtMapper.selectList(null);
        if(tmList == null)
            for(String tag : tags)
                mtMapper.insert(new TagModelEntity(null, tag));
        else
            for(String tag : tags)
                if(!haveTag(tmList, tag))
                    mtMapper.insert(new TagModelEntity(null, tag));
    }

    private boolean haveTag(List<TagModelEntity> tms, String tag) {
        for(TagModelEntity tm : tms)
            if(tm.getName().equals(tag))
                return true;
        return false;
    }

    @Override
    public List<GetModelVo> getPaperModelList() {
        List<PaperModelEntity> pmList = modelMapper.selectList(null);
        List<GetModelVo> mVos = new ArrayList<>();
        for(PaperModelEntity pm : pmList) {
            GetModelVo mVo = new GetModelVo();
            mVo.setId(pm.getId());
            mVo.setDesc(pm.getDetail());
            mVo.setTitle(pm.getTitle());
            mVo.setFromUser(pm.getFromUser());
            mVo.setTags(StringUtils.String2List(pm.getTags()));
            mVos.add(mVo);
        }
        return mVos;
    }

    @Override
    public List<GetTagVo> getTagList() {
        List<TagModelEntity> list = mtMapper.selectList(null);
        List<GetTagVo> tagVos = new ArrayList<>();
        for(TagModelEntity tm : list) {
            GetTagVo tagVo = new GetTagVo();
            tagVo.setValue(tm.getName());
            tagVo.setLabel(tm.getName());
            tagVos.add(tagVo);
        }
        return tagVos;
    }

    @Override
    public AddPaperVo getModelInfoById(String modelId) {
        // 1.根据模板id查询模板问卷
        PaperModelEntity model = modelMapper.selectById(modelId);

        // 2.将模板信息保存到返回对象
        AddPaperVo paperVo = new AddPaperVo();
        paperVo.setTitle(model.getTitle());
        paperVo.setFooter(model.getFooter());

        // 3.根据模板id查询该模板的题目信息
        List<QsModelEntity> qsList = mqMapper.selectList(new QueryWrapper<QsModelEntity>().eq("mp_id", modelId));

        // 4.将题目列表按qs_order字段排序
        qsList.sort(Comparator.comparingInt(QsModelEntity::getQsOrder));

        // 5.将题目信息保存到返回对象
        paperVo.setQs_count(qsList.size());
        List<AddQuestionVo> qsVos = new ArrayList<>();
        for(QsModelEntity qs : qsList) {
            AddQuestionVo qsVo = new AddQuestionVo();
            qsVo.setQs_id(qs.getId());
            qsVo.setQs_order(qs.getQsOrder());
            qsVo.setQs_number(qs.getQsOrder());
            qsVo.setQs_title(qs.getQsTitle());
            qsVo.setQs_type(qs.getQsType()+"");
            qsVo.setQs_option(StringUtils.String2List(qs.getQsOption()));
            qsVos.add(qsVo);
        }

        paperVo.setContent(qsVos);
        return paperVo;
    }

    @Override
    public AddQuestionVo getQuestionInfoByModelIdAndIndex(String modelId, int i) {
        List<QsModelEntity> qsList = mqMapper.selectList(new QueryWrapper<QsModelEntity>().eq("mp_id", modelId));
        qsList.sort(Comparator.comparingInt(QsModelEntity::getQsOrder));
        AddQuestionVo qsVo = new AddQuestionVo();
        qsVo.setQs_title(qsList.get(i).getQsTitle());
        qsVo.setQs_option(StringUtils.String2List(qsList.get(i).getQsOption()));
        return qsVo;
    }

    @Override
    public List<GetModelVo> getPaperModelListByCond(SearchModelVo smVo) {
        QueryWrapper<PaperModelEntity> wrapper = new QueryWrapper<>();
        if(!org.apache.commons.lang3.StringUtils.isBlank(smVo.getTitle()))
            wrapper.like("title", smVo.getTitle());
        if(smVo.getTags().size() > 0)
            for(String tag : smVo.getTags())
                wrapper.like("tags",tag);
        List<PaperModelEntity> pmList = modelMapper.selectList(wrapper);
        List<GetModelVo> mVos = new ArrayList<>();
        for(PaperModelEntity pm : pmList) {
            GetModelVo mVo = new GetModelVo();
            mVo.setId(pm.getId());
            mVo.setDesc(pm.getDetail());
            mVo.setTitle(pm.getTitle());
            mVo.setFromUser(pm.getFromUser());
            mVo.setTags(StringUtils.String2List(pm.getTags()));
            mVos.add(mVo);
        }
        return mVos;
    }
}
