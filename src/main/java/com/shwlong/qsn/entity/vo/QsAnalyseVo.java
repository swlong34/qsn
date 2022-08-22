package com.shwlong.qsn.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QsAnalyseVo {
    private Integer qs_order;
    private String qs_title;
    private String qs_type;
    private Integer submit_person;
    private List<Map<String,String>> qs_option; // option subtotal percentage
}
