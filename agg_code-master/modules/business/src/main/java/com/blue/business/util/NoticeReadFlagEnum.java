package com.blue.business.util;

/**
 * @Description: 通知是否必读的枚举
 * @ClassName: NoticeReadFlag
 * @Author: liuliu
 * @Date: 2022/3/24 11:31
 */
public enum NoticeReadFlagEnum {

    DEFAULT_READ_FLAG(0, "非必读"),
    READ_FLAG(1, "必读");

    private Integer readCode;
    private String state;

    NoticeReadFlagEnum(Integer readCode, String state) {
        this.readCode = readCode;
        this.state = state;
    }

    public Integer getReadCode() {
        return readCode;
    }

}
