package com.shwlong.qsn.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class AddPaperVo {

    private String paper_id;
    private int qs_count;
    private String title;
    private List<AddQuestionVo> content;
    private String footer;
    private String start_time;
    private String end_time;
    private Integer status;

}
