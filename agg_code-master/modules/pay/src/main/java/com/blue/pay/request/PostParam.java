package com.blue.pay.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("农信银行下单参数")
@Data
public class PostParam {

    @ApiModelProperty("商户号")
    private String mchtNo;

    @ApiModelProperty("下单金额")
    private String orderAmt;

    @ApiModelProperty("openId")
    private String openId;

    @ApiModelProperty("支付类型 1.微信 2.支付宝 3.银联")
    private String orderType;

    @ApiModelProperty("商户ID")
    private String mchId;



}
