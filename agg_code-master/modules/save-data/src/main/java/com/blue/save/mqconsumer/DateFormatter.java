package com.blue.save.mqconsumer;

import java.util.Date;

/**
 * @author fcq
 * @version v2.0.2.consumer
 * @description 日期格式化
 * @date 2022/3/25 15:39
 */
public class DateFormatter {

    public static Date format(Date origin) {
        if (null == origin) {
            return null;
        }
        long l = (origin.getTime() / 1000) * 1000;
        return new Date(l);
    }
}
