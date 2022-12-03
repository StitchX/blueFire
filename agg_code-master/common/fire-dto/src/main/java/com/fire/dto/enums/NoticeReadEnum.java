package com.fire.dto.enums;

/**
 * @Description: 通知发送类型
 * @ClassName: NoticeSendTypeEnum
 * @Author: liuliu
 * @Date: 2022/3/1 12:14
 */
public enum NoticeReadEnum {

    NOTICE_READ_DEFAULT("0", "不必读"),
    NOTICE_READ("1", "必读");

    private final String code;

    private final String msg;

    NoticeReadEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String code() {
        return code;
    }

}
