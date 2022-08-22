package com.shwlong.qsn.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateModelVo {

    private String paperId;
    private String user;
    private String desc;
    private List<String> tags;

}
