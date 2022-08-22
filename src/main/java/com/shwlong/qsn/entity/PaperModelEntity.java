package com.shwlong.qsn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("model_paper")
public class PaperModelEntity {

    private String id;
    private String title;
    private String detail;
    private String footer;
    private Date createTime;
    private String fromUser;
    private String tags;

}
