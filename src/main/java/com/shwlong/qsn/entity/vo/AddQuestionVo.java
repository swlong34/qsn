package com.shwlong.qsn.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class AddQuestionVo {

    private String qs_id;
    private int qs_order;
    private int qs_number;
    private String qs_title;
    private String qs_type;
    private List<String> qs_option;
    private int required;

}
