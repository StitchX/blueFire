package com.blue.nongxin.entity;

import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "订单表实体")
@Data
public class AppWisdomNongxinPay {

        private Long orderId;
    
        @ApiModelProperty(value = "商户名称")
        private String merchantName;
    
        @ApiModelProperty(value = "支付金额 精确到分")
        private Double price;
    
        @ApiModelProperty(value = "状态 1.待支付 2.已支付 3.支付失败 4.订单关闭")
        private Integer status;
    
        @ApiModelProperty(value = "关联商户id")
        private Long merchantId;
    
        @ApiModelProperty(value = "柜台代码")
        private String posId;
    
        @ApiModelProperty(value = "银行名称")
        private String bankName;
    
        @ApiModelProperty(value = "银行编码")
        private String bankCode;
    
        @ApiModelProperty(value = "分行代码")
        private String branchId;
    
        @ApiModelProperty(value = "创建时间")
        private Date createTime;
    
        @ApiModelProperty(value = "回调时间")
        private Date callbackTime;
    
        @ApiModelProperty(value = "用户open_id")
        private String openId;
    
        @ApiModelProperty(value = "支付类型 1.微信 2.支付宝 3.银联")
        private Integer type;
    
        @ApiModelProperty(value = "银行订单号 reqSsn")
        private String bankOrderId;
    
        @ApiModelProperty(value = "第三方订单号 origReqSsn")
        private String otherOrderId;
    
        @ApiModelProperty(value = "银行商户号")
        private String mchtNo;
    
        @ApiModelProperty(value = "下单时间 用于查询订单时使用")
        private String reqTime;
    


}
