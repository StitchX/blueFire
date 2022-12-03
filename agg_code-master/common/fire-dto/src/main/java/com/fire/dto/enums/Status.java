package com.fire.dto.enums;

/**
 * @Description:
 * @ClassName: Status
 * @Author: liuliu
 * @Date: 2022/1/5 16:38
 */
public enum Status {
    SUCCESS("200", "成功"),
    FAILURE("501", "失败"),

    //业务参数
    PARAM_LOSS("101", "请求参数缺失"),
    MERCHANT_ID_LOSS("102","商户订单号缺失"),
    MERCHANT_NOT_EXISTS("404","该商户不存在");

    private final String status;

    private final String message;

    Status(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String status() {
        return status;
    }

    public String message() {
        return message;
    }
}
