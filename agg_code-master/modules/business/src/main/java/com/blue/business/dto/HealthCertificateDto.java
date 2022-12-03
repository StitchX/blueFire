package com.blue.business.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @date: 2022-03-21 11:28
 */
@ApiModel("健康证数据传输对象")
@Data
public class HealthCertificateDto {

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

    @ApiModelProperty("当前页")
    private Integer current;

    @ApiModelProperty("每页条数")
    private Integer size;

}
