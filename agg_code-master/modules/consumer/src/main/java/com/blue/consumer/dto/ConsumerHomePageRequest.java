package com.blue.consumer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author DK 2022/3/8 14:31
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Api(tags = "消费者端首页接口请求参数")
public class ConsumerHomePageRequest {

    @ApiModelProperty("微信/支付宝用户唯一标识")
    private String openId;

    @ApiModelProperty(value = "商户id",required = true)
    private String merchantId;

    @ApiModelProperty("终端类型：1-微信，2-支付宝")
    private String terminalType;
}
