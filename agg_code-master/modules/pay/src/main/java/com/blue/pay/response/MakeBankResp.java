package com.blue.pay.response;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

@ApiModel("银行下单返回参数")
@Data
public class MakeBankResp {


    @ApiModelProperty("返回码 返回00为成功 其他为失败")
    private String respCode;

    @ApiModelProperty("返回信息")
    private String respMsg;

    @ApiModelProperty("下单号")
    private String origReqSsn;

    @ApiModelProperty("回调参数内容")
    private String resCentet;

    @ApiModelProperty("回调类型  1.直接返回支付URL 2.返回微信或支付宝参数 3.JS脚本")
    private String resType;

    @ApiModelProperty("下单时间 yyyyMMddHHmmss")
    private String reqTime;
}
