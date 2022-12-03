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
 * @createTime : [2022/2/11 18:09]
 */
@Data
@TableName("wx_user_info")
@ApiModel("微信用户")
public class WxUserInfo {
    @TableId(type = IdType.NONE)
    private String wxUserId;
    private Long merchantId;
    private String nickName;
    private String openId;
    private String appId;
    private String country;
    private String province;
    private String city;
    private Integer gender;
    private String headImgUrl;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Timestamp incomingDate;
    private Integer isSubscribe;
}
