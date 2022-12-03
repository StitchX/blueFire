package com.blue.gongshang;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.fire.dto", "com.blue.gongshang", "com.fire.common"}, exclude = {DataSourceAutoConfiguration.class})
@MapperScan({"com.blue.gongshang.mapper"})
@EnableDiscoveryClient
@EnableTransactionManagement(proxyTargetClass = true)
@EnableSwagger2Doc
@EnableScheduling
public class GongshangApplication {

    public static void main(String[] args) {
        SpringApplication.run(GongshangApplication.class, args);
    }

}
