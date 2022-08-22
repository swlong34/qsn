package com.shwlong.qsn.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetModelVo {

    private String id;
    private String title;
    private String desc;
    private List<String> tags;
    private String fromUser;

}
