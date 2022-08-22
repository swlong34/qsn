package com.shwlong.qsn.entity.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SaveToExcelVo {

    private Integer index;

    private Date submitTime;

    private List<SaveAnswerVo> ans;

}
