package com.blue.crm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorEnum {
    SYSTEM_ERROR(999999, "系统内部错误"),
    LOGIN_ERROR(100001, "登录出现异常"),
    UN_SIGNED(100002, "未登录"),
    INVALID_PARAM(100003, "参数异常");

    private Integer code;
    private String desc;
}
