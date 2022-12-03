package com.fire.admin.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

/**
 * @author: admin
 * @Description:
 * @date: 2022-01-24 11:56
 */
@ApiModel("操作日志请求参数")
@Data
public class LogInfoDto {

    @ApiModelProperty("监管(局)所编号")
    private Set<Long> supervisionIds;

    @ApiModelProperty(value = "开始时间", example = "2022-01-24 00:00:00")
    private String startTime;

    @ApiModelProperty(value = "结束时间", example = "2022-01-24 00:00:00")
    private String endTime;

    @ApiModelProperty("操作人")
    private String name;

    @ApiModelProperty("当前页码")
    private Long current;

    @ApiModelProperty("每页显示条数")
    private Long size;
}
