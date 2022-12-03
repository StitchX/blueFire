package com.blue.business.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @Description:
 * @date: 2022-03-21 12:14
 */
@ApiModel("从业证视图对象")
@Builder
@Data
public class EmploymentCertificateVo {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("从业证图片地址")
    private String imageUrl;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("身份证")
    private String identityCard;

    @ApiModelProperty("证照类型")
    private String type;

    @ApiModelProperty("取证日期")
    private String receiveDate;

    @ApiModelProperty("有效期")
    private String validPeriod;

}
