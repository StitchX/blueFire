package com.fire.dto.enums;

/**
 * @Description: 通知发送枚举
 * @ClassName: NoticeScopeEnum
 * @Author: liuliu
 * @Date: 2022/2/25 17:28
 */
public enum NoticeStatusEnum {

    NO_SEND_NOTICE(0, "待发"),
    SUCCESS_SEND_NOTICE(1, "已发");


    private final Integer sendCode;

    private final String sendMsg;

    NoticeStatusEnum(Integer sendCode, String sendMsg) {
        this.sendCode = sendCode;
        this.sendMsg = sendMsg;
    }

    public Integer sendCode() {
        return sendCode;
    }

    public String sendMsg() {
        return sendMsg;
    }
}
