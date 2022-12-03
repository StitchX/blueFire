package com.blue.business.util;

import cn.hutool.core.util.StrUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 手机号验证工具类
 * @date: 2022-03-22 11:53
 */
public class CheckMobilePhoneUtil {

    public static boolean CheckMobilePhone(String phoneNum) {
        String regex = "^(1[3-9]\\d{9}$)";
        if (StrUtil.isNotBlank(phoneNum)) {
            if (phoneNum.trim().length() == 11) {
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(phoneNum);
                if (m.matches()) {
                    return true;
                }
            }
        }
        return false;
    }
}
