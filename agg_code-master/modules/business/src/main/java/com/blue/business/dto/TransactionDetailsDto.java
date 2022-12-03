package com.blue.business.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description:
 * @date: 2022-03-22 14:47
 */
@ApiModel("交易明细查询数据传输对象")
@Data
public class TransactionDetailsDto {

    @ApiModelProperty("查询时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date queryDate;

    @ApiModelProperty("当前页")
    private Integer current;

    @ApiModelProperty("每页条数")
    private Integer size;
}
