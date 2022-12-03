package com.blue.business.dto;

import lombok.Data;

/**
 * @Description: 登录及认证相关网络传输对象
 * @ClassName: AuthDto
 * @Author: liuliu
 * @Date: 2022/3/21 14:46
 */
@Data
public class AuthDto {

    /**
     * 登录的手机号码
     */
    private String phone;

    /**
     * 登录的验证码
     */
    private String smsCode;
}
