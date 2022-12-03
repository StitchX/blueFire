package com.blue.consumer.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author DK 2022/3/8 14:44
 */
@Data
@Api("消费者端公共请求参数")
public class ConsumerCommonRequest {

    @ApiModelProperty(value = "商户id",required = true)
    private String merchantId;
}
