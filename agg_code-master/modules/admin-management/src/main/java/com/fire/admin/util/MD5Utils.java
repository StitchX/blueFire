package com.fire.admin.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @classname: MD5Utils
 * @description MD5 加密工具类
 * @author: liu liu
 * @create: 2020-08-20 09:45
 */
@Slf4j
public class MD5Utils {

    static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    static final char hexDigitsLower[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    /**
     * @Description: 对字符串 MD5 无盐值加密
     * @Param: plainText 传入要加密的字符串
     * @return: MD5加密后生成32位(小写字母 + 数字)字符串
     * @Author: liu liu
     * @date: 2020/8/20 9:45
     */
    public static String MD5Lower(String plainText) {
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 使用指定的字节更新摘要
            md.update(plainText.getBytes());

            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值。1 固定值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            log.error("MD5无盐值加密错误：", e);
            return null;
        }
    }


    /**
     * @Description: 对字符串 MD5 加密
     * @Param: plainText 传入要加密的字符串
     * @return: MD5加密后生成32位(大写字母 + 数字)字符串
     * @Author: liu liu
     * @date: 2020/8/20 9:47
     */
    public static String MD5Upper(String plainText) {
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 使用指定的字节更新摘要
            md.update(plainText.getBytes());

            // 获得密文
            byte[] mdResult = md.digest();
            // 把密文转换成十六进制的字符串形式
            int j = mdResult.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = mdResult[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];// 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
            }
            return new String(str);
        } catch (Exception e) {
            log.error("MD5加密错误", e);
            return null;
        }
    }


}
