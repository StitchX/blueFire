package com.fire.admin.rest;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.fire.admin.dto.NoticeScheduleDto;
import com.fire.admin.service.ScheduleService;
import com.fire.admin.task.SendNoticeJob;
import com.fire.dto.response.BaseRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Description:
 * @ClassName: ScheduleController
 * @Author: liuliu
 * @Date: 2022/3/4 10:35
 */
@Api(tags = "定时任务调度相关接口")
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation("定时发送邮件")
    @PostMapping("/sendEmail")
    public BaseRestResponse sendEmail(@RequestBody NoticeScheduleDto noticeScheduleDto) {
        Date date = DateUtil.parse(noticeScheduleDto.getSendTime(), DatePattern.NORM_DATETIME_FORMAT);
        String jobName = scheduleService.scheduleFixTimeJob(SendNoticeJob.class, date, noticeScheduleDto);
        return new BaseRestResponse(jobName);
    }


    @ApiOperation("取消定时任务")
    @PostMapping("/cancelScheduleJob")
    public BaseRestResponse cancelScheduleJob(@RequestParam String jobName) {
        Boolean success = scheduleService.cancelScheduleJob(jobName);
        return new BaseRestResponse(success);
    }


}
