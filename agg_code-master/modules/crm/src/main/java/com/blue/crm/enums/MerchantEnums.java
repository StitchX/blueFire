package com.blue.crm.enums;

import lombok.AllArgsConstructor;
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
        SETTLED(1),
        APPOINTMENT(2),
        USED(4);

        private Integer code;
    }
}
