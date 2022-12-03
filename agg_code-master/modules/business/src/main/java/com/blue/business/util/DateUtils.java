package com.blue.business.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * @Description: 时间格式化转换工具类
 * @date: 2022-02-14 18:20
 */
public class DateUtils {

    public static long getCurrentTimestamp() {
        return new Date().getTime();
    }
    /**
     * 获取日期开始时间戳
     * @param date
     * @return
     */
    public static long getDayBeginTimestamp(Date date) {
        DateTime dateTime = DateUtil.beginOfDay(date);
        long time = dateTime.toTimestamp().getTime();
        return time;
    }

    /**
     * 获取日期结束时间戳
     * @param date
     * @return
     */
    public static long getDayEndTimestamp(Date date) {
        DateTime dateTime = DateUtil.endOfDay(date);
        long time = dateTime.toTimestamp().getTime();
        return time;
    }

    /**
     * 获取前一天开始时间戳
     * @param date
     * @return
     */
    public static long getBeforeDayBeginTimestamp(Date date) {
        DateTime dateTime = DateUtil.endOfDay(date);
        DateTime dateTime1 = DateUtil.offsetDay(dateTime, -1);
        long time = dateTime1.toTimestamp().getTime();
        return time;
    }

    /**
     * 获取前一天结束时间戳
     * @param date
     * @return
     */
    public static long getBeforeDayEndTimestamp(Date date) {
        DateTime dateTime = DateUtil.endOfDay(date);
        DateTime dateTime1 = DateUtil.offsetDay(dateTime, -1);
        long time = dateTime1.toTimestamp().getTime();
        return time;
    }

    /**
     * 获取周开始时间戳
     * @param date
     * @return
     */
    public static long getWeekBeginTimestamp(Date date) {
        DateTime dateTime = DateUtil.beginOfWeek(date);
        long time = dateTime.toTimestamp().getTime();
        return time;
    }

    /**
     * 获取周结束时间戳
     * @param date
     * @return
     */
    public static long getWeekEndTimestamp(Date date) {
        DateTime dateTime = DateUtil.endOfWeek(date);
        long time = dateTime.toTimestamp().getTime();
        return time;
    }

    /**
     * 获取前一周结束时间戳
     * @param date
     * @return
     */
    public static long getBeforeWeekBeginTimestamp(Date date) {
        DateTime dateTime = DateUtil.endOfWeek(date);
        DateTime dateTime1 = DateUtil.offsetWeek(dateTime, -1);
        long time = dateTime1.toTimestamp().getTime();
        return time;
    }

    /**
     * 获取前一周结束时间戳
     * @param date
     * @return
     */
    public static long getBeforeWeekEndTimestamp(Date date) {
        DateTime dateTime = DateUtil.endOfWeek(date);
        DateTime dateTime1 = DateUtil.offsetWeek(dateTime, -1);
        long time = dateTime1.toTimestamp().getTime();
        return time;
    }

    /**
     * 获取月开始时间戳
     * @param date
     * @return
     */
    public static long getMonthBeginTimestamp(Date date) {
        DateTime dateTime = DateUtil.beginOfMonth(date);
        long time = dateTime.toTimestamp().getTime();
        return time;
    }

    /**
     * 获取月结束时间戳
     * @param date
     * @return
     */
    public static long getMonthEndTimestamp(Date date) {
        DateTime dateTime = DateUtil.endOfMonth(date);
        long time = dateTime.toTimestamp().getTime();
        return time;
    }

    /**
     * 获取前一月开始时间戳
     * @param date
     * @return
     */
    public static long getBeforeMonthBeginTimestamp(Date date) {
        DateTime dateTime = DateUtil.endOfMonth(date);
        DateTime dateTime1 = DateUtil.offsetMonth(dateTime, -1);
        long time = dateTime1.toTimestamp().getTime();
        return time;
    }

    /**
     * 获取前一月结束时间戳
     * @param date
     * @return
     */
    public static long getBeforeMonthEndTimestamp(Date date) {
        DateTime dateTime = DateUtil.endOfMonth(date);
        DateTime dateTime1 = DateUtil.offsetMonth(dateTime, -1);
        long time = dateTime1.toTimestamp().getTime();
        return time;
    }

    /**
     * 获取年开始时间戳
     * @param date
     * @return
     */
    public static long getYearBeginTimestamp(Date date) {
        DateTime dateTime = DateUtil.beginOfYear(date);
        long time = dateTime.toTimestamp().getTime();
        return time;
    }

    /**
     * 获取年结束时间戳
     * @param date
     * @return
     */
    public static long getYearEndTimestamp(Date date) {
        DateTime dateTime = DateUtil.endOfYear(date);
        long time = dateTime.toTimestamp().getTime();
        return time;
    }

    /**
     * 获取前一年开始时间戳
     * @param date
     * @return
     */
    public static long getBeforeYearBeginTimestamp(Date date) {
        DateTime dateTime = DateUtil.beginOfYear(date);
        DateTime dateTime1 = DateUtil.offset(dateTime, DateField.YEAR, -1);
        long time = dateTime1.toTimestamp().getTime();
        return time;
    }

    /**
     * 获取前一年结束时间戳
     * @param date
     * @return
     */
    public static long getBeforeYearEndTimestamp(Date date) {
        DateTime dateTime = DateUtil.beginOfYear(date);
        DateTime dateTime1 = DateUtil.offset(dateTime, DateField.YEAR, -1);
        long time = dateTime1.toTimestamp().getTime();
        return time;
    }

}
