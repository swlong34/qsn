package com.shwlong.qsn.service;

import com.shwlong.qsn.entity.PaperEntity;
import com.shwlong.qsn.entity.vo.AddAnswerVo;
import com.shwlong.qsn.entity.vo.AddPaperVo;
import com.shwlong.qsn.entity.vo.GetPaperVo;
import com.shwlong.qsn.entity.vo.QsAnalyseVo;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface PaperService {


    /**
     * 保存用户的问卷数据
     * @param paperVo
     * @return
     */
    void savePaper(int userId, AddPaperVo paperVo);

    /**
     * 编辑问卷
     * @param paperVo
     */
    void editPaper(int userId, AddPaperVo paperVo);

    /**
     * 添加一个问卷
     * @param paper
     * @return 返回问卷id
     */
    String addOnePaper(PaperEntity paper);

    /**
     * 获取所有问卷列表
     * @return
     */
    List<PaperEntity> getPaperList();

    /**
     * 根据用户id获取问卷列表
     * @param userId
     * @return
     */
    List<GetPaperVo> getPaperListByUserId(int userId);

    /**
     * 根据用户id和问卷标题查询问卷 模糊查询
     * @param userId
     * @param text
     * @return
     */
    List<GetPaperVo> getPaperListByUserIdAndTitle(int userId, String text);

    /**
     * 根据问卷id获取问卷信息
     * @param paperId
     * @return
     */
    AddPaperVo getPaperInfoById(String paperId);

    /**
     * 根据问卷id更新问卷状态
     * @param paperId
     * @param isPublished
     */
    void updateStatusByPaperId(String paperId, Integer isPublished);

    /**
     * 根据用户id和问卷状态查询问卷列表
     * @param userId
     * @param status
     * @return
     */
    List<GetPaperVo> getPaperListByUserIdAndStatus(int userId, Integer status);

    /**
     * 保存问卷答案
     * @param paperId
     * @param ansVos
     */
    void saveAnswerByPaperId(String paperId, List<AddAnswerVo> ansVos);

    /**
     * 根据问卷id获取问卷答案分析结果
     * @param paperId
     * @return
     */
    List<QsAnalyseVo> getPaperAnalyseInfoByPaperId(String paperId);

    /**
     * 根据问卷id获取问卷信息
     * @param paperId
     * @return
     */
    PaperEntity getPaperById(String paperId);

    /**
     * 删除问卷及数据
     * @param paperId
     */
    void delPaperInfoAndData(String paperId);

    /**
     * 获取所有已发布问卷
     * @param isPublished
     * @return
     */
    List<PaperEntity> getPaperListByStatus(Integer isPublished);

    /**
     * 下载问卷数据
     * @param paperId
     */
    XSSFWorkbook downloadExcel(String paperId) throws IOException;

    /**
     * 保存问卷信息到word文档
     * @param paperId
     */
    XWPFDocument downloadWord(String paperId) throws IOException;

    /**
     * 根据问卷id和截止时间更新问卷
     * @param paperId
     * @param date
     */
    void updateEndTimeByPaperId(String paperId, Date date);
}
