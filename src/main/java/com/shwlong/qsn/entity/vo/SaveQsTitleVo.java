package com.shwlong.qsn.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveQsTitleVo {

    private Integer qsOrder;
    private Integer qsType;
    private String qsTitle;

}
