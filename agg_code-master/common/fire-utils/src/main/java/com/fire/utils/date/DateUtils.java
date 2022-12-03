package com.fire.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期工具
 *
 * @author ZJQ 2021年5月14日17:54:47
 */
public class DateUtils {

    /**
     * 老系统时间戳转long的毫秒
     *
     * @param dateStr 日期字符串
     * @return Long
     */
    public static Long String2Long(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        try {
            Date date = sdf.parse(dateStr);
            return date.getTime();
        } catch (ParseException e) {
            return 0L;
        }
    }

    /**
     * 老系统时间戳获取
     *
     * @return String
     */
    public static String oldTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(new Date());
    }

    /**
     * @Description: 当前系统时间转换
     * @Param: [localDateTime]
     * @return: java.lang.String
     * @Author: liuliu
     * @Date: 2021/5/18 14:13
     */
    public static String strformatDatetime(LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime);
    }


    public static String formatDate(LocalDate date) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(date);
    }


    public static String formatDateStr(LocalDate date) {
        return DateTimeFormatter.ofPattern("yyyy年MM月dd日").format(date);
    }


    /**
     * 按yyyyMMdd获取当天时间
     * @return
     */
    public static String getToday(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(new Date());
    }



}
