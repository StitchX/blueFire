package com.fire.admin.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 时间格式化转换工具类
 * @date: 2022-02-14 18:20
 */
public class DateUtil {

    public static String getFirstDayOfMonth(String date) {
        Map<String, Integer> map = formatDate(date);

        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, map.get("year"));

        //设置月份
        cal.set(Calendar.MONTH, map.get("month") - 1);

        //获取某月最小天数
        int firstDay = cal.getMinimum(Calendar.DATE);

        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);

        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        return sdf.format(cal.getTime());
    }

    public static String getLastDayOfMonth(String date) {
        Map<String, Integer> map = formatDate(date);

        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, map.get("year"));

        //设置月份
        cal.set(Calendar.MONTH, map.get("month") - 1);

        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DATE);

        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);

        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(cal.getTime());
    }

    private static Map<String, Integer> formatDate(String date) {
        String[] split = date.split("-");
        Map<String, Integer> map = new HashMap<>();
        map.put("year", Integer.parseInt(split[0]));
        map.put("month", Integer.parseInt(split[1]));
        return map;
    }

    public static String formatLocalDateTime(LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime);
    }

    public static String formatLocalDate(LocalDate localDate) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(localDate);
    }

    public static LocalDateTime formatLocalDateTimeStr(String time) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(time, df);
    }

}
