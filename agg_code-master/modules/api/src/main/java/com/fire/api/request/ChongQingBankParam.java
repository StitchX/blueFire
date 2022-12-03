package com.fire.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: admin
 * @Description:
 * @date: 2022-01-04 14:53
 */
@ApiModel("重庆银行相关功能请求数据传输对象")
@Data
public class ChongQingBankParam {

    @ApiModelProperty("重庆银行商户id")
    //@NotNull(message = "商户id不能为空！")
    private String businessId ;

    @ApiModelProperty("放心码商户id")
    //@NotNull(message = "放心码商户id不能为空！")
    private Long codeId;

    @ApiModelProperty("重庆银行商户秘钥")
    //@NotNull(message = "商户秘钥不能为空！")
    private String chongQingSecret;

    @ApiModelProperty("商户名称")
    private String businessName;

    @ApiModelProperty("添加人")
    private String creator;

    @ApiModelProperty("商户添加时间")
    private String createTime;

    @ApiModelProperty("当前页码")
    private Long current;

    @ApiModelProperty("每页显示条数")
    private Long size;

    @ApiModelProperty("id")
    private Long id;

}
