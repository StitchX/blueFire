package com.fire.common.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Data
@RefreshScope
@Configuration
//@ConfigurationProperties(prefix = "tencent",ignoreInvalidFields = true)
public class TencentConfig {

    @Value("${tencent.bucket-name:null}")
    private String bucketName;

    @Value("${tencent.secret-id:null}")
    private String secretId;

    @Value("${tencent.secret-key:null}")
    private String secretKey;

    @Value("${tencent.region-name:null}")
    private String regionName;

}
