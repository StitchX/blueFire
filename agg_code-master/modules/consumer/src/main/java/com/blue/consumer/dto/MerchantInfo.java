package com.blue.consumer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DK 2022/3/8 17:22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Api("商户信息")
public class MerchantInfo {

    @ApiModelProperty("商户名称")
    private String merchantName;

    @ApiModelProperty("风险等级")
    private String riskLevel;

    @ApiModelProperty("是否加入食品安全险：0-未加入；1-已加入；空字符-不展示食品安全险title")
    private String isInsurance;

    @ApiModelProperty("社会信用代码")
    private String socialCreditCode;

    @ApiModelProperty("法人/负责人姓名")
    private String representName;

    @ApiModelProperty("监管所名称")
    private String supervisionName;



}
