package com.fire.dto.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("business_license")
@ApiModel("食品经营许可证")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusinessLicense implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long businessLicenseId;
    @ApiModelProperty("商户id")
    private Long merchantId;
    @ApiModelProperty("身份证")
    private String idCard;
    @ApiModelProperty("图片地址")
    private String imagesUrl;
    @ApiModelProperty("门店名称")
    @NotBlank(message = "店铺名称不能为空")
    @Length(max = 128, message = "门店名称字数不能超过128")
    private String storeName;
    @ApiModelProperty("法人姓名")
    @NotBlank(message = "法人姓名不能为空")
    @Length(max = 32, message = "法人姓名字数不能超过32")
    private String representName;
    @ApiModelProperty("社会信用代码")
    @NotBlank(message = "社会信用代码不能为空")
    @Length(max = 32, message = "社会信用代码字数不能超过32")
    private String socialCreditCode;
    @ApiModelProperty("经营范围")
    @NotBlank(message = "经营类型不能为空")
    @Length(max = 50, message = "经营类型字数不能超过50")
    private String businessScope;
    @ApiModelProperty("详细地址")
    @NotBlank(message = "营业执照地址不能为空")
    @Length(max = 256, message = "营业执照地址字数不能超过256")
    private String detailedAddress;

    private String creator;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Timestamp createTime;
    private String updater;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Timestamp updateTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private LocalDate validPeriod;

    @TableField(exist = false)
    @ApiModelProperty("是否永久有效")
    private boolean validPermanent;
}
