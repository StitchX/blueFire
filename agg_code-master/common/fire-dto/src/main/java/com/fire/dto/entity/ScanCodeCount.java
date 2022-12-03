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
 * @description 扫码记录实体
 * @date 2022/3/10 10:02
 */
@Data
@TableName("scan_code_count")
public class ScanCodeCount {

    /**
     * 自增id
     */
    @Id
    @TableId
    private Long id;

    /**
     * 商户id
     */
    @TableField("merchant_id")
    private String merchantId;

    /**
     * 头像 url
     */
    private String avatar;

    /**
     * 微信/支付宝 OPENID
     */
    @TableField("open_id")
    private String openId;

    /**
     * 昵称
     */
    @TableField("nick_name")
    private String nickName;

    /**
     * 扫码时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 终端类型 1 微信， 2 支付宝
     */
    private String terminal;
}
