package com.fire.admin.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/1/27 17:36]
 */
public class CommonUtil {

    public static String handlePhoneNumber(String phoneNumber){
        if(StringUtils.isNotEmpty(phoneNumber)){
            return phoneNumber.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
        }
        return phoneNumber;
    }
}
