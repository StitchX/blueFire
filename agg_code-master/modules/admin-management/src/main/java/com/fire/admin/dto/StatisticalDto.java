package com.fire.admin.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @date: 2022-02-10 13:23
 */
@ApiModel("统计接口数据传递对象")
@Data
public class StatisticalDto {

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("活跃商户接口月份选择对象")
    private String weekTime;

}
