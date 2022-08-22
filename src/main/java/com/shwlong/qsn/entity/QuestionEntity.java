package com.shwlong.qsn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("question")
public class QuestionEntity {

    private String id;

    private String paperId;
    private Integer qsType;
    private String qsTitle;
    private Integer qsOrder;
    private String qsOption;
    private String qsAnswer;
    private Integer required;

}
