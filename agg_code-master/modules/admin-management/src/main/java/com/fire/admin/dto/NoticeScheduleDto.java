package com.fire.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;


/**
 * @Description: 创建任务调度的条件
 * @ClassName: NoticeScheduleDto
 * @Author: liuliu
 * @Date: 2022/3/4 10:52
 */
@Data
@Builder
public class NoticeScheduleDto {

    @ApiModelProperty("通知编号(主键)")
    private Long noticeId;

    @ApiModelProperty("通知标题")
    private String noticeTitle;

    @ApiModelProperty("通知发送时间")
    private String sendTime;


}
