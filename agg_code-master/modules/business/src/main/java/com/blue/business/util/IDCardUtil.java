package com.blue.business.util;

import cn.hutool.core.util.StrUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 身份证验证工具类(15位 、 18位数字)，最后一位是校验位，可能为数字或字符X(x)
 * @date: 2022-03-26 16:20
 */
public class IDCardUtil {

    public static boolean checkIdCard(String cardNum) {
        String regex = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
        if (StrUtil.isNotBlank(cardNum)) {
            if (cardNum.trim().length() == 15 || cardNum.trim().length() == 18) {
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(cardNum);
                if (m.matches()) {
                    return true;
                }
            }
        }
        return false;
    }

}



