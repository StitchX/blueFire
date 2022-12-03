package com.fire.admin.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fire.admin.dto.NoticeScheduleExecuteDto;
import com.fire.admin.entity.NoticeDetailsInfo;
import com.fire.admin.entity.NoticeInfo;
import com.fire.admin.service.NoticeDetailsService;
import com.fire.admin.service.NoticeService;
import com.fire.admin.service.ScheduleService;
import com.fire.admin.util.SecurityUtil;
import com.fire.dto.enums.NoticeStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Description:
 * @ClassName: SendEmailJob
 * @Author: liuliu
 * @Date: 2022/3/4 10:11
 */
@Slf4j
@Component
public class SendNoticeJob extends QuartzJobBean {

    private ScheduleService scheduleService;

    private NoticeService noticeService;

    private NoticeDetailsService noticeDetailsService;

    @Autowired
    public SendNoticeJob(ScheduleService scheduleService, NoticeService noticeService, NoticeDetailsService noticeDetailsService) {
        this.scheduleService = scheduleService;
        this.noticeService = noticeService;
        this.noticeDetailsService = noticeDetailsService;
    }


    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Trigger trigger = jobExecutionContext.getTrigger();
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        Long noticeId = StrUtil.isNotEmpty(jobDataMap.getString("sendNotice")) ? Long.parseLong(jobDataMap.getString("sendNotice")) : null;
        Timestamp sendTime = new Timestamp(jobExecutionContext.getFireTime().getTime());
        log.info("发起通知，任务编号为：{}，任务名称：{}", noticeId, trigger.getJobKey().getName());
        saveNoticeDetailsBatch(trigger, noticeId, sendTime);
    }


    private void saveNoticeDetailsBatch(Trigger trigger, Long noticeId, Timestamp sendTime) {
        NoticeScheduleExecuteDto executeDto = noticeService.querySendNoticeById(noticeId);
        if (ObjectUtil.isEmpty(executeDto)) {
            log.info("该通知已经删除，没有从数据库中获取到该通知信息。删除该任务");
            scheduleService.cancelScheduleJob(trigger.getKey().getName());
        }
        if (ObjectUtil.isNotEmpty(executeDto) && CollectionUtil.isNotEmpty(executeDto.getMerchantIds())) {
            List<NoticeDetailsInfo> list = new ArrayList<>();
            executeDto.getMerchantIds().forEach(id -> {
                NoticeDetailsInfo detailsInfo = NoticeDetailsInfo.builder().noticeId(noticeId)
                        .merchantId(id)
                        .supervisionId(executeDto.getSupervisionId())
                        .msgSendTime(sendTime).build();
                list.add(detailsInfo);
            });
            noticeDetailsService.saveNoticeDetailBatch(list);
            //完成后删除触发器和任务
            scheduleService.cancelScheduleJob(trigger.getKey().getName());
            log.info("通知发送完毕");
            // 通知发送完毕，修改通知状态为已发送的状态
            noticeService.updateById(NoticeInfo.builder().noticeId(noticeId).status(NoticeStatusEnum.SUCCESS_SEND_NOTICE.sendCode()).build());
        }

    }

}
