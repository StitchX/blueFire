package com.fire.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * @Description: 任务调度执行的条件
 * @ClassName: NoticeScheduleExecuteDto
 * @Author: liuliu
 * @Date: 2022/3/4 19:15
 */
@Data
@Builder
public class NoticeScheduleExecuteDto {

    @ApiModelProperty("监管(局)所ID")
    private Long supervisionId;

    @ApiModelProperty("需要发送通知的商户集合")
    private Set<Long> merchantIds;

}
