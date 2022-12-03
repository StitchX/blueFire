package com.blue.code.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author DK 2022/2/23 14:13
 */

@Configuration
@RefreshScope
@Data
public class ErCodeConfig {

    /**
     * 商户信息码水印地址
     */
    @Value("${ercode.watermarkInfoImage}")
    private  String watermarkInfoImage;

    /**
     * 支付码水印地址
     */
    @Value("${ercode.watermarkPayImage}")
    private  String watermarkPayImage;

    /**
     * 空码水印地址
     */
    @Value("${ercode.watermarkEmptyImage}")
    private  String watermarkEmptyImage;

    /**
     * 商户信息码H5地址
     */
    @Value("${ercode.infoCodeUrl}")
    private  String infoCodeUrl;

    /**
     * 支付码H5地址
     */
    @Value("${ercode.payCodeUrl}")
    private  String payCodeUrl;



    /**
     * 商户信息码腾讯云COS存储文件夹地址
     */
    @Value("${ercode.infoCodePath}")
    private String infoCodePath;


    /**
     * 支付码腾讯云COS存储文件夹地址
     */
    @Value("${ercode.payCodePath}")
    private String payCodePath;

    /**
     * 支付码腾讯云COS存储文件夹地址
     */
    @Value("${ercode.emptyCodePath}")
    private String emptyCodePath;

}
