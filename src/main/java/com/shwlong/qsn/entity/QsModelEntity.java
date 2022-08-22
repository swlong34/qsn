package com.shwlong.qsn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("model_question")
public class QsModelEntity {

    private String id;
    private String mpId;
    private Integer qsType;
    private String qsTitle;
    private Integer qsOrder;
    private String qsOption;

}
