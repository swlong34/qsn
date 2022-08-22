package com.shwlong.qsn.controller;

import com.shwlong.qsn.entity.TagModelEntity;
import com.shwlong.qsn.entity.vo.*;
import com.shwlong.qsn.service.ModelService;
import com.shwlong.qsn.util.QsnEnum;
import com.shwlong.qsn.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/model")
@RestController
public class ModelController {

    @Autowired
    private ModelService modelService;

    /**
     * 创建问卷模板
     * @param modelVo
     * @return
     */
    @RequestMapping("/create")
    public R createPaperModel(@RequestBody CreateModelVo modelVo) {
        modelService.createPaperModelByModelVo(modelVo);
        return R.ok(QsnEnum.MODEL_CREATE_SUCCESS.getMsg());
    }

    /**
     * 获取问卷模板列表
     * @return
     */
    @RequestMapping("/list")
    public R getModelList() {
        List<GetModelVo> list = modelService.getPaperModelList();
        return R.ok().put("data", list);
    }

    /**
     * 根据查询条件获取问卷模板列表
     * @param smVo
     * @return
     */
    @RequestMapping("/listby")
    public R getModelListBy(@RequestBody SearchModelVo smVo) {
        List<GetModelVo> list = modelService.getPaperModelListByCond(smVo);
        return R.ok().put("data", list);
    }

    /**
     * 获取所有标签信息
     * @return
     */
    @RequestMapping("/tag/list")
    public R getTags() {
        List<GetTagVo> list = modelService.getTagList();
        return R.ok().put("data", list);
    }

    /**
     * 根据模板id获取问卷信息
     * @param modelId
     * @return
     */
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public R getPaperInfo(@PathVariable("id") String modelId) {
        AddPaperVo paperInfo = modelService.getModelInfoById(modelId);
        return R.ok().put("data", paperInfo);
    }

    /**
     * 根据模板id和题目顺序获取题目信息
     *
     * @return
     */
    @RequestMapping("/qs/info/{id}/{index}")
    public R getQuestionInfo(@PathVariable("id") String modelId, @PathVariable("index") int index) {
        AddQuestionVo qsVo = modelService.getQuestionInfoByModelIdAndIndex(modelId, index - 1);
        return R.ok().put("data", qsVo);
    }

}
