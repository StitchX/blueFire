package com.blue.business.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description:
 * @date: 2022-03-21 10:27
 */
@ApiModel("从业证实体类")
@TableName("employment_certificate")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmploymentCertificate {

    @ApiModelProperty("从业证编号")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("商户编号")
    private Long merchantId;

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

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("是否删除 0-正常 1-已删除")
    @TableLogic
    private short isDeleted;
}
