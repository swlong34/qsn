package com.shwlong.qsn.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPaperVo {

    private String id;
    private String paperTitle;
    private Integer paperStatus;
    private Date createTime;
    private Date startTime;
    private Date endTime;
    private Integer ansPapers;

}
