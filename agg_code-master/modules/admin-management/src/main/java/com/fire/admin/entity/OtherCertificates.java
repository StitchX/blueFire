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
 * @createTime : [2022/2/11 17:56]
 */
@Data
@TableName("other_certificates")
@ApiModel("其他证件")
public class OtherCertificates {
    @TableId(type = IdType.NONE)
    private Long otherCertificatesId;
    private Long merchantId;
    private String name;
    private String imagesUrl;
    private String creator;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Timestamp createTime;
    private String updater;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Timestamp updateTime;
    private String remark;
}
