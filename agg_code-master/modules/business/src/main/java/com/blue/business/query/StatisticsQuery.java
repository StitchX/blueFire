package com.blue.business.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author fcq
 * @version v2.0.8.business
 * @description 统计请求参数
 * @date 2022/3/23 22:04
 */
@Data
@ApiModel("统计请求参数")
public class StatisticsQuery {

    @ApiModelProperty("类型")
    private String type = "day";

    @ApiModelProperty("日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date date = new Date();
}
