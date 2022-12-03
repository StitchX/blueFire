package com.fire.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import com.fire.admin.dto.NoticeScheduleDto;
import com.fire.admin.service.ScheduleService;
import com.fire.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Description:
 * @ClassName: ScheduleServiceImpl
 * @Author: liuliu
 * @Date: 2022/3/4 10:34
 */
@Slf4j
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private Scheduler scheduler;
    private String defaultGroup = "default_group";

    @Override
    public String scheduleJob(Class<? extends Job> jobBeanClass, String startTime, NoticeScheduleDto noticeScheduleDto) {
        // 首先删除相同任务名称的通知
        if (cancelScheduleJob(noticeScheduleDto.getNoticeTitle())) {
            // 创建需要执行的任务
            String jobName = noticeScheduleDto.getNoticeTitle();
            JobDetail jobDetail = JobBuilder.newJob(jobBeanClass)
                    .withIdentity(jobName, defaultGroup)
                    .usingJobData("sendNotice", noticeScheduleDto.getNoticeId().toString())
                    .build();
            //创建触发器，指定任务执行时间
            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(jobName, defaultGroup)
                    .withSchedule(CronScheduleBuilder.cronSchedule(startTime))
                    .build();
            //使用调度器进行任务调度
            try {
                scheduler.scheduleJob(jobDetail, cronTrigger);
            } catch (SchedulerException e) {
                log.info("创建定时任务失败，通知发送时间不能小于当前系统时间");
                throw new BaseException("新增通知失败，通知时间必须大于当前系统时间");
            }
            return jobName;
        }
        return null;
    }

    @Override
    public String scheduleFixTimeJob(Class<? extends Job> jobBeanClass, Date startTime, NoticeScheduleDto noticeScheduleDto) {

        //日期转CRON表达式
        String startCron = String.format("%d %d %d %d %d ? %d",
                DateUtil.second(startTime),
                DateUtil.minute(startTime),
                DateUtil.hour(startTime, true),
                DateUtil.dayOfMonth(startTime),
                DateUtil.month(startTime) + 1,
                DateUtil.year(startTime));
        return scheduleJob(jobBeanClass, startCron, noticeScheduleDto);
    }

    @Override
    public Boolean cancelScheduleJob(String jobName) {
        boolean success = false;
        try {
            // 暂停触发器
            scheduler.pauseTrigger(new TriggerKey(jobName, defaultGroup));
            // 移除触发器中的任务
            scheduler.unscheduleJob(new TriggerKey(jobName, defaultGroup));
            // 删除任务
            scheduler.deleteJob(new JobKey(jobName, defaultGroup));
            success = true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return success;
    }

}
