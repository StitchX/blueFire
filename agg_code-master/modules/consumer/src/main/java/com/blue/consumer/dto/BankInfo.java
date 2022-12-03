package com.blue.consumer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Dk 2022/3/8 20:40
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Api("银行信息")
public class BankInfo {

    @ApiModelProperty("银行logo图片url")
    private String bankLogoUrl;

    @ApiModelProperty("银行名称")
    private String bankName;


}
