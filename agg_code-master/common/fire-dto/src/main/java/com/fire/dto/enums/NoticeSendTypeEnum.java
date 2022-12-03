package com.fire.dto.enums;

/**
 * @Description: 通知发送类型
 * @ClassName: NoticeSendTypeEnum
 * @Author: liuliu
 * @Date: 2022/3/1 12:14
 */
public enum NoticeSendTypeEnum {

    NOW_SEND(1, "立即发送"),
    TIMING_SEND_NOTICE(2, "定时发送通知");

    private final Integer sendCode;

    private final String sendDesc;

    NoticeSendTypeEnum(Integer sendCode, String sendDesc) {
        this.sendCode = sendCode;
        this.sendDesc = sendDesc;
    }

    public Integer sendCode(){
        return sendCode;
    }

}
