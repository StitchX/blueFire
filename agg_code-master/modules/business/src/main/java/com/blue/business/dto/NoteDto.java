package com.blue.business.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @Description: 获取短信验证码请求参数
 * @ClassName: NoteDto
 * @Author: liuliu
 * @Date: 2022/3/21 11:30
 */
@Data
@Builder
public class NoteDto {
    /**
     * 账号
     */
    public String account;

    /**
     * 验证密匙
     */
    public String pswd;

    /**
     * 接入号
     */
    public String ext;

    /**
     * 消息响应格式
     */
    public String rf;

    /**
     * 发送的消息
     */
    public String msg;

    /**
     * 发送的电话号码
     */
    public String pn;

    /**
     * @param msgPrefix 消息前缀
     * @param msgSuffix 消息后缀
     * @param code      验证码
     * @description: 构建消息
     * @return: void
     * @author: liuliu
     * @date: 2022-03-14 23:12
     */
    public String formatMsg(String msgPrefix, String msgSuffix, String code) {
        return this.msg = msgPrefix.concat(code).concat(msgSuffix);
    }


}
