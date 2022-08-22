package com.shwlong.qsn.util;

import net.sf.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

    /**
     * 将字符串转换成字符串数组
     *
     * @param str
     * @return
     */
    public static List<String> String2List(String str) {
        Object[] objs = JSONArray.fromObject(str).toArray();
        List<String> strList = new ArrayList<>();
        for (Object obj : objs) {
            strList.add(obj.toString());
        }
        return strList;
    }

    /**
     * 将题目类型数字转换成对应文字描述
     *
     * @param type
     * @return
     */
    public static String qsType2String(Integer type) {
        String name = null;
        if (type.equals(Constant.SELECT_RADIO)) name = "单选题";
        else if (type.equals(Constant.SELECT_MULTI)) name = "多选题";
        else if (type.equals(Constant.SELECT_DROP)) name = "下拉题";
        else if (type.equals(Constant.FILL_SINGLE)) name = "单行文本";
        else if (type.equals(Constant.FILL_MULTI)) name = "多行文本";
        else if (type.equals(Constant.FILL_REGION)) name = "地区";
        else if (type.equals(Constant.FILL_DATE)) name = "日期";
        else name = "模板题";
        return name;
    }

    /**
     * 根据题目描述返回类型
     * @return
     */
    public static Integer string2QsType(String title) {
        int type = 0;
        if(title.contains("[单选")) type= Constant.SELECT_RADIO;
        else if(title.contains("[多选")) type= Constant.SELECT_MULTI;
        else if(title.contains("[下拉")) type= Constant.SELECT_DROP;
        else if(title.contains("[单行")) type= Constant.FILL_SINGLE;
        else if(title.contains("[多行")) type= Constant.FILL_MULTI;
        else if(title.contains("[地区")) type= Constant.FILL_SINGLE;
        else if(title.contains("[日期")) type= Constant.FILL_SINGLE;
        else {
            if (title.contains("姓名")) type= Constant.SHORTCUT_NAME;
            if (title.contains("学号/工号")) type= Constant.SHORTCUT_NUMBER;
            if (title.contains("性别")) type= Constant.SHORTCUT_GENDER;
            if (title.contains("出生日期")) type= Constant.SHORTCUT_BIRTHDAY;
            if (title.contains("籍贯")) type= Constant.SHORTCUT_ORIGIN;
            if (title.contains("手机号")) type= Constant.SHORTCUT_PHONE;
            if (title.contains("邮箱")) type= Constant.SHORTCUT_EMAIL;
        }
        return type;
    }

}
