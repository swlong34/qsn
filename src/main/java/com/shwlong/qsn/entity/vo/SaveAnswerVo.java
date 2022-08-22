package com.shwlong.qsn.entity.vo;

import com.shwlong.qsn.util.Constant;
import com.shwlong.qsn.util.StringUtils;
import lombok.Data;

import java.util.List;

@Data
public class SaveAnswerVo {

    private Integer qsOrder;
    private Integer qsType;
    private String options;

    public void setOptions(String options) {
        StringBuilder strs = new StringBuilder();
        if(this.qsType.equals(Constant.SELECT_MULTI)) {
            List<String> list = StringUtils.String2List(options);
            for (String str : list)
                strs.append(str).append(";");
        } else {
            strs.append(options.replace("\"",""));
        }
        this.options = strs.toString();
    }
}
