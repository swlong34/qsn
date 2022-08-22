package com.shwlong.qsn.controller;

import com.shwlong.qsn.entity.PaperEntity;
import com.shwlong.qsn.entity.vo.AddAnswerVo;
import com.shwlong.qsn.entity.vo.AddPaperVo;
import com.shwlong.qsn.entity.vo.GetPaperVo;
import com.shwlong.qsn.entity.vo.QsAnalyseVo;
import com.shwlong.qsn.exception.QsnException;
import com.shwlong.qsn.service.PaperService;
import com.shwlong.qsn.util.Constant;
import com.shwlong.qsn.util.DateTimeUtils;
import com.shwlong.qsn.util.QsnEnum;
import com.shwlong.qsn.util.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RequestMapping("/paper")
@RestController
public class PaperController {

    @Autowired
    private PaperService paperService;

    /**
     * 统计所有问卷数量
     * @return
     */
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public R getPaperCount() {
        List<PaperEntity> list = paperService.getPaperList();
        return R.ok().put("data", list.size());
    }

    /**
     * 根据用户id获取问卷列表
     * @return
     */
    @RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
    public R list(@PathVariable("id") int userId) {
        List<GetPaperVo> list = paperService.getPaperListByUserId(userId);
        return R.ok().put("data", list);
    }

    /**
     * 根据用户id和title获取问卷列表
     * @return
     */
    @RequestMapping(value = "/list/title/{id}", method = RequestMethod.GET)
    public R list(@PathVariable("id") int userId, @RequestParam("cond") String text) {
        List<GetPaperVo> list = paperService.getPaperListByUserIdAndTitle(userId, text);
        return R.ok().put("data", list);
    }

    /**
     * 根据用户id和status获取问卷列表
     * @return
     */
    @RequestMapping(value = "/list/status/{id}", method = RequestMethod.GET)
    public R list(@PathVariable("id") int userId, @RequestParam("status") Integer status) {
        List<GetPaperVo> list = paperService.getPaperListByUserIdAndStatus(userId, status);
        return R.ok().put("data", list);
    }

    /**
     * 根据问卷id获取问卷信息 校验token
     * @param paperId
     * @return
     */
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public R getPaperInfo(@PathVariable("id") String paperId) {
        AddPaperVo paperInfo = paperService.getPaperInfoById(paperId);
        return R.ok().put("data", paperInfo);
    }

    /**
     * 根据问卷id获取问卷信息 不校验token
     * @param paperId
     * @return
     */
    @RequestMapping(value = "/info/fill/{id}", method = RequestMethod.GET)
    public R getPaperInfoNoToken(@PathVariable("id") String paperId) {
        PaperEntity paper = paperService.getPaperById(paperId);
        if(paper.getPaperStatus().equals(Constant.NO_PUBLISH))
            throw new QsnException(QsnEnum.PAPER_NO_PUBLISH.getMsg());
        if(paper.getPaperStatus().equals(Constant.IS_OVERDUE))
            throw new QsnException(QsnEnum.PAPER_IS_STOP.getMsg());
        AddPaperVo paperInfo = paperService.getPaperInfoById(paperId);
        return R.ok().put("data", paperInfo);
    }

    /**
     * 添加 & 编辑问卷
     * @RequestBody：用来封装前端传来的JSON数据
     * @param paperVo
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public R savePaper(@RequestBody AddPaperVo paperVo,
                       HttpServletRequest request) {
        int userId = (int) request.getAttribute(Constant.USER_ID);
        // 若paper_id为空，则添加问卷；否则，编辑问卷
        if(StringUtils.isBlank(paperVo.getPaper_id())) {
            paperService.savePaper(userId, paperVo);
            return R.ok(QsnEnum.PAPER_CREATE_FINISH.getMsg());
        } else {
            paperService.editPaper(userId, paperVo);
            return R.ok(QsnEnum.PAPER_EDIT_FINISH.getMsg());
        }
    }

    /**
     * 修改问卷状态
     * @param paperId
     * @return
     */
    @RequestMapping(value = "/update/status/{id}", method = RequestMethod.PUT)
    public R publicPaper(@PathVariable("id") String paperId, @RequestParam("status") Integer status) {
        paperService.updateStatusByPaperId(paperId, status);
        if(status == Constant.IS_PUBLISHED)
            return R.ok(QsnEnum.PAPER_PUBLIC_SUCCESS.getMsg());
        else
            return R.error(500, QsnEnum.PAPER_STOP_RECLAIM.getMsg());
    }

    /**
     * 保存问卷答案
     * @param paperId
     * @param ansVos
     * @return
     */
    @RequestMapping(value = "/save/answer/{id}",method = RequestMethod.POST)
    public R savePaperAnswer(@PathVariable("id") String paperId, @RequestBody List<AddAnswerVo> ansVos) {
        paperService.saveAnswerByPaperId(paperId, ansVos);
        return R.ok(QsnEnum.PAPER_ANSWER_IS_SUBMIT.getMsg());
    }

    /**
     * 获取答卷统计信息
     * @param paperId
     * @return
     */
    @RequestMapping(value = "/get/analyse/{id}", method = RequestMethod.GET)
    public R getPaperAnalyseInfo(@PathVariable("id") String paperId) {
        PaperEntity paper = paperService.getPaperById(paperId);
        List<QsAnalyseVo> qsAnVos = paperService.getPaperAnalyseInfoByPaperId(paperId);
        return R.ok().put("title", paper.getPaperTitle()).put("data", qsAnVos);
    }

    @RequestMapping(value = "/del/{id}", method = RequestMethod.DELETE)
    public R delPaperInfoAndData(@PathVariable("id") String paperId) {
        paperService.delPaperInfoAndData(paperId);
        return R.ok(QsnEnum.PAPER_DELETE_SUCCESS.getMsg());
    }

    @RequestMapping(value = "/get/end/flag/{id}")
    public R getFlagByPaperEndTime(@PathVariable("id") String paperId) {
        PaperEntity paper = paperService.getPaperById(paperId);
        boolean flag = false;
        // 问卷已结束，重新发布需要修改截止时间
        if(paper.getEndTime().getTime() - new Date().getTime() <= 0) flag = true;
        return R.ok().put("data", flag);
    }

    @RequestMapping(value = "/update/end/{id}", method = RequestMethod.PUT)
    public R updatePaperByEndTime(@PathVariable("id") String paperId, String endTime) {
        Date date = DateTimeUtils.str2Date(endTime);
        paperService.updateEndTimeByPaperId(paperId, date);
        return R.ok();
    }

}
