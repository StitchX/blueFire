package com.blue.pay.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: QGC
 * @Description:
 * @date: 2022-03-09 14:53
 */
@ApiModel("银行下单参数")
@Data
public class MakeBankParam {

    @ApiModelProperty("商户id")
    //@NotNull(message = "商户id不能为空！")
    private String businessId;

    @ApiModelProperty("订单金额")
    //NotNull(message = "订单金额不能为空")
    private String orderAmt;

    @ApiModelProperty("openId")
    private String openId;

    @ApiModelProperty("支付类型 1.微信 2.支付宝")
    private String orderType;


}
