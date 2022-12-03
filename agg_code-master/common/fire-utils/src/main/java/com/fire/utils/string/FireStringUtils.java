package com.fire.utils.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author ZJQ 2021年5月17日11:02:32
 */
public class FireStringUtils {

    /**
     * 手机号验证
     *
     * @param str 手机号
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        boolean b = false;
        if (null == str || "".equals(str)) {
            return b;
        }
        Pattern p = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$"); // 验证手机号
        Matcher m = p.matcher(str);
        b = m.matches();
        return b;
    }

}
