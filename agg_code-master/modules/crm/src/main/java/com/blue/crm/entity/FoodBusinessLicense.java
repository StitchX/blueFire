package com.blue.crm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/2/24 11:27]
 */

@TableName("food_business_license")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FoodBusinessLicense {
    @TableId(type = IdType.AUTO)
    private Long foodBusinessLicenseId;
    private Long merchantId;
    @ApiModelProperty("企业名称")
    private String enterpriseName;
    @ApiModelProperty("企业法人")
    private String enterpriseLegalPerson;
    @ApiModelProperty("社会信用代码")
    private String socialCreditCode;
    @ApiModelProperty("许可证号")
    private String licenseKey;
    @ApiModelProperty("发证机关")
    private String issuingAuthority;
    @ApiModelProperty("发起日期")
    private Timestamp initiationDate;
    @ApiModelProperty("有效期")
    private Timestamp validityTerm;
    @ApiModelProperty("主体业态")
    private String mainBusinessType;
    @ApiModelProperty("经营类别")
    private String businessCategory;
    @ApiModelProperty("经营范围")
    private String businessScope;
    @ApiModelProperty("区域")
    private String area;
    @ApiModelProperty("所属街道")
    private String street;
    @ApiModelProperty("监督管理机构")
    private String supervision;
    @ApiModelProperty("上传者")
    private String creator;
    @ApiModelProperty("上传时间")
    private Timestamp createTime;
    @ApiModelProperty("修改者")
    private String updater;
    @ApiModelProperty("修改时间")
    private Timestamp updateTime;
    @ApiModelProperty("图片地址")
    private String imagesUrl;
    @ApiModelProperty("删除标记")
    private Integer isDelete;

    @ApiModelProperty(value = "有效期限至")
    @TableField(exist = false)
    private String validToDate;
}
