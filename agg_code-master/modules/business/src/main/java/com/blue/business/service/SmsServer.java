package com.blue.business.service;

import org.springframework.stereotype.Service;

/**
 * @Description: 用户登录获取验证码service
 * @ClassName: SmsServer
 * @Author: liuliu
 * @Date: 2022/3/21 11:34
 */

@Service
public interface SmsServer {

    /**
     *@description:  获取短信验证码
     * @param phone 电话号码
     *@return: java.lang.String
     *@author: liuliu
     *@date: 2022-03-21 11:35
    */
    void sendMsg(String phone);

}
