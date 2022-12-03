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
 * @date: 2022-03-21 10:38
 */
@ApiModel("健康证实体类")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("health_certificate")
public class HealthCertificate {

    @ApiModelProperty("健康证编号")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("商户编号")
    private Long merchantId;

    @ApiModelProperty("健康证图片地址")
    private String imageUrl;

    @ApiModelProperty("健康证名称")
    private String name;

    @ApiModelProperty("健康证人员手机号")
    private String phone;

    @ApiModelProperty("身份证")
    private String identityCard;

    @ApiModelProperty("职位")
    private String position;

    @ApiModelProperty("取证日期")
    private String receiveDate;

    @ApiModelProperty("有效期")
    private String validPeriod;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty("是否删除 0-正常 1-已删除")
    @TableLogic
    private short isDeleted;
}
