package com.fire.common.listener;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;


/**
 * 自定义错误日志拦截器
 *
 * @author ZJQ
 * @apiNote 这里拦截器需要配合logback中的配置，加拦截器com.fire.common.listener.ErrorListener
 */
public class ErrorListener extends UnsynchronizedAppenderBase<ILoggingEvent> {

    /**
     * 记录时间点,时间戳除以100000 表示每100秒记录一轮
     */
    public static Long time = 0L;
    /**
     * 记录次数
     */
    public static Integer count = 1;

    @Override
    protected void append(ILoggingEvent event) {
        Level level = event.getLevel();
        if (level.equals(Level.ERROR)) {
            //当前时间
            Long thisTime = System.currentTimeMillis() / 100000;
            //如果还在记录时间范围内 那么次数加1
            if (time.equals(thisTime)) {
                count++;
            } else {
                //如果已过时间那么次数归零
                time = thisTime;
                count = 1;
            }
        }
    }
}
