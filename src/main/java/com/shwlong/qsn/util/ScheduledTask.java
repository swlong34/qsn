package com.shwlong.qsn.util;

import com.shwlong.qsn.entity.PaperEntity;
import com.shwlong.qsn.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Component
public class ScheduledTask {

    @Autowired
    private PaperService paperService;

    /**
     * 更新问卷状态
     * @return
     */
    /*@Scheduled(cron = "0/5 * * * * 0-6")
    public void updatePaperStatus() {
        // 获取所有已发布问卷
        List<PaperEntity> isPubPapers = paperService.getPaperListByStatus(Constant.IS_PUBLISHED);
        for(PaperEntity paper : isPubPapers) {
            // 若问卷结束时间在当前时间之前，表示问卷已停止回收
            if(paper.getEndTime().getTime() - new Date().getTime() < 0) {
                paperService.updateStatusByPaperId(paper.getId(), Constant.IS_OVERDUE);
            }
        }
    }*/

}
