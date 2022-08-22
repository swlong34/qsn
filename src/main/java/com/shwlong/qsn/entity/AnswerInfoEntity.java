package com.shwlong.qsn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("answer_info")
public class AnswerInfoEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String paperId;
    private Date submitTime;
    private String fromIp;

}
