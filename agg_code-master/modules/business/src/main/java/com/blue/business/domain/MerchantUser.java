package com.blue.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @ClassName: MerchantUser
 * @Author: liuliu
 * @Date: 2022/3/21 18:06
 */
@ApiModel("短信验证码登录对应的表")
@Data
@TableName("merchant_user")
public class MerchantUser {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value = "登录用户名，在这里是手机号码")
    private String username;
    @ApiModelProperty(value = "商户编号")
    private Long merchantId;
    private String createTime;
    private String updateTime;
    private Integer lockFlag;
    private Integer delFlag;


}
