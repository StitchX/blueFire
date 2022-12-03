package com.fire.admin.service;


import com.fire.admin.dto.NoticeScheduleDto;
import org.quartz.Job;

import java.util.Date;

/**
 * @Description:
 * @ClassName: ScheduleService
 * @Author: liuliu
 * @Date: 2022/3/4 10:11
 */
public interface ScheduleService {

    /**
     * 通过CRON表达式调度任务
     */
    String scheduleJob(Class<? extends Job> jobBeanClass, String startTime, NoticeScheduleDto noticeScheduleDto);

    /**
     * 调度指定时间的任务
     */
    String scheduleFixTimeJob(Class<? extends Job> jobBeanClass, Date startTime, NoticeScheduleDto noticeScheduleDto);

    /**
     * 取消定时任务
     */
    Boolean cancelScheduleJob(String jobName);

}
