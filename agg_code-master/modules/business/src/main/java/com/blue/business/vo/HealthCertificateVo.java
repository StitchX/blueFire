package com.blue.business.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @Description:
 * @date: 2022-03-21 12:10
 */
@ApiModel("健康证视图对象")
@Data
@Builder
public class HealthCertificateVo {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("图片地址")
    private String imageUrl;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("身份证")
    private String identityCard;

    @ApiModelProperty("职位")
    private String position;

    @ApiModelProperty("取证日期")
    private String receiveDate;

    @ApiModelProperty("有效期")
    private String validPeriod;
}
