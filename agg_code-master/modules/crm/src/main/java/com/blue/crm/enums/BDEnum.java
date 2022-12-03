package com.blue.crm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/3/23 10:41]
 */
@AllArgsConstructor
@Getter
public enum BDEnum {
    BD(0,"BD"),
    BDM(1, "BDM");

    private Integer code;
    private String desc;
}
