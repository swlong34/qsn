package com.shwlong.qsn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("paper")
public class PaperEntity {

    private String id;

    private Integer userId;
    private String paperTitle;
    private String paperFooter;
    private Integer paperStatus;
    private Date createTime;
    private Date startTime;
    private Date endTime;
    private Integer submitPerson;

}
