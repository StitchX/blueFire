package com.fire.dto.mqDto;

import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "商户扫码实体")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScanCode {

    
        @ApiModelProperty(value = "头像 url")
        private String avatar;
    
        @ApiModelProperty(value = "微信/支付宝 OPENID")
        private String openId;
    
        @ApiModelProperty(value = "昵称")
        private String nickName;
    
        @ApiModelProperty(value = "扫码时间")
        private String createTime;
    
        @ApiModelProperty(value = "终端类型 1 微信， 2 支付宝")
        private Integer terminal;
    
        @ApiModelProperty(value = "商户id")
        private Long merchantId;
    


}
