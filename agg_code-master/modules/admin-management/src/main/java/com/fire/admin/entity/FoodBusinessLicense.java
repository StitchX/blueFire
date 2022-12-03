package com.fire.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/2/11 18:32]
 */
@Data
@TableName("food_business_license")
@ApiModel("食品经营许可证")
public class FoodBusinessLicense {
    @TableId(type = IdType.NONE)
    private Long foodBusinessLicenseId;

    private Long merchantId;
    private String enterpriseName;
    private String enterpriseLegalPerson;
    private String socialCreditCode;
    private String licenseKey;
    private String issuingAuthority;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Timestamp validityTerm;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Timestamp initiationDate;
    private String mainBusinessType;
    private String businessCategory;
    private String businessScope;
    private String area;
    private String street;
    private String supervision;
    private String creator;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Timestamp createTime;
    private String updater;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Timestamp updateTime;
    private String imagesUrl;
    @TableLogic(value = "0", delval = "1")
    private Integer isDelete;

}
