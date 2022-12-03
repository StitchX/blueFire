package com.blue.consumer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DK 2022/3/10 11:07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("经营信息查询")
public class BusinessInfoResponse {

    @ApiModelProperty(value = "商户名称")
    private String merchantName;

    @ApiModelProperty(value = "社会信用代码")
    private String socialCreditCode;

    @ApiModelProperty(value = "法人/负责人")
    private String representName;

    @ApiModelProperty(value = "联系电话")
    private String linkedPhone;

    @ApiModelProperty(value = "行业分类")
    private String industryClassification;

    @ApiModelProperty(value = "经营地址")
    private String businessAddress;

    @ApiModelProperty(value = "监管所")
    private String supervisionName;

    @ApiModelProperty(value = "营业执照图片url")
    private String businessUrl;
}
