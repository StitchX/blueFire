package com.blue.business.config;

import lombok.Data;

/**
 * @Description: 短信验证码配置信息
 * @ClassName: NoteConfig
 * @Author: liuliu
 * @Date: 2022/3/21 11:29
 */
@Data
public class NoteConfig {

    /**
     * 请求url
     */
    public static final String url = "http://139.155.181.183:8001/mt.ashx";

    /**
     * 账号
     */
    public static final String account = "800101";

    /**
     * 验证密匙
     */
    public static final String pswd = "AZPkr4cd";

    /**
     * 接入号
     */
    public static final String ext = "106908101";

    /**
     * 消息响应格式
     */
    public static final String rf = "json";


    /**
     * 消息前缀
     */
    public static final String msgPrefix = "【智慧监管系统】您正在登录智慧监管平台账号，验证码是：";

    /**
     * 消息后最
     */
    public static final String msgSuffix = "，5分钟内有效，请及时输入。";

    public static final String result = "0";

}
