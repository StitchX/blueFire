package com.fire.api.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel(value = "Mqtt支付消息")
@Data
public class MqttPayParam{

    @ApiModelProperty(value = "商户ID")
    @NotNull(message = "商户ID 不能为空")
    private String merchantID;

    @ApiModelProperty(value = "支付类型")
    @NotNull(message = "支付类型 不能为空")
    private String type;

    @ApiModelProperty(value = "金额")
    @NotNull(message = "金额 不能为空")
    private String price;

    @ApiModelProperty(value = "订单号")
    @NotNull(message = "订单号 不能为空")
    private String orderID;

    @ApiModelProperty(value = "sign")
    @NotNull(message = "sign 不能为空")
    private String sign;

    @ApiModelProperty(value = "accountingDate")
    @NotNull(message = "payToAccTime 不能为空")
    private long payToAccTime;




}
