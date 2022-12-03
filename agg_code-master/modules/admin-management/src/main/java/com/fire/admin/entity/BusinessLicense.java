package com.fire.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/2/11 17:35]
 */
@Data
@TableName("business_license")
@ApiModel("营业执照")
public class BusinessLicense {
    @TableId(type = IdType.NONE)
    private Long businessLicenseId;
    private Long merchantId;
    private String socialCreditCode;
    private String storeName;
    private String detailedAddress;
    private String imagesUrl;
    private String representName;
    private String businessScope;
    private String idCard;
    private String creator;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Timestamp createTime;
    private String updater;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Timestamp updateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Timestamp validPeriod;
}
