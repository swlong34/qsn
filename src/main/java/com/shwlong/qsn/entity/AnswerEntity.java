package com.shwlong.qsn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("answer")
public class AnswerEntity {

    private String id;
    private String paperId;
    private String qsId;
    private Integer qsType;
    private String answerOption;
    private Integer saveId;

}
