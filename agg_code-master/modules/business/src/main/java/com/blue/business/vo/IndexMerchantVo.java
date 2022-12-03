package com.blue.business.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/3/24 15:08]
 */

@ApiModel("首页商户信息")
@Data
public class IndexMerchantVo {
    @ApiModelProperty("店铺名称")
    private String storeName;
    @ApiModelProperty("商户ID")
    private Long merchantId;
    @ApiModelProperty("支付二维码图片地址")
    private String payCodeUrl;
    @ApiModelProperty("食品安全状态:0未开通,1开通中,2已开通")
    private Integer foodSafetyStatus;
    @ApiModelProperty("银行支付状态:0未开通,1开通中,2已开通")
    private Integer payStatus;
}
