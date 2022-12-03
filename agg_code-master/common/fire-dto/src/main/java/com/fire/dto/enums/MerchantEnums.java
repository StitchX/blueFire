package com.fire.dto.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/3/9 10:49]
 */
public class MerchantEnums {
    @Getter
    @AllArgsConstructor
    public enum Status{
        SETTLED(1), //已入驻
        APPOINTMENT(2),  //已预约
        USED(4);  //已使用

        private Integer code;
    }
}
