package com.shwlong.qsn.util;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 中国标准时间转换成LocalDateTime
     * @param dateStr
     * @return
     */
    public static Date str2Date(String dateStr) {
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) { }
        return date;
    }

    public static String dateStr2FormatStr(String dateStr) {
        SimpleDateFormat sim1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        SimpleDateFormat sim2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ans = null;
        try {
            Date date = sim1.parse(dateStr);
            ans = sim2.format(date);
        }catch (ParseException e ){ }
        return ans;
    }

    public static String date2FormatStr(Date date) {
        SimpleDateFormat sim2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sim2.format(date);
    }




}
