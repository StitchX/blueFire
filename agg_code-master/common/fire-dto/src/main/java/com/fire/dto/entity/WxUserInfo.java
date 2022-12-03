package com.fire.dto.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * @author fcq
 * @version v2.0.2.consumer
 * @description 支付宝、微信授权信息表
 * @date 2022/3/14 10:04
 */
@Data
@TableName("wx_user_info")
public class WxUserInfo {

    /**
     * 主键
     */
    @Id
    @TableId("wx_user_id")
    private String wxUserId;

    /**
     * 商户id
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 微信昵称
     */
    @TableField("nick_name")
    private String nickName;

    /**
     * 应用唯一标识
     */
    @TableField("open_id")
    private String openId;

    /**
     * 合作方id
     */
    @TableField("app_id")
    private String appId;

    /**
     * 国家
     */
    private String country;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 用户头像图片地址
     */
    @TableField("head_img_url")
    private String headImgUrl;

    /**
     * 收款日期
     */
    @TableField("incoming_date")
    private Date incomingDate;

    /**
     * 是否订阅 0：是 1：否
     */
    @TableField("is_subscribe")
    private String isSubscribe;
}
