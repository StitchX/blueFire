package com.blue.consumer.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 消费者端配置类
 * @author DK 2022/3/9 10:04
 */

@Data
@Configuration
@RefreshScope
public class ConsumerConfig {

    /**
     * 显示食品保险的区域
     */
    @Value("${consumer.insuranceArea:null}")
    private List<String> insuranceArea;

    /**
     * 不显示健康码的区域
     */
    @Value("${consumer.healthCodeArea:null}")
    private List<String>  healthCodeArea;

    /**
     * 银行logo图片url
     */
    @Value("${consumer.bankUrl:null}")
    private String bankUrl;

    /**
     * 健康码logo图片url
     */
    @Value("${consumer.healthUrl:null}")
    private String healthUrl;


}
