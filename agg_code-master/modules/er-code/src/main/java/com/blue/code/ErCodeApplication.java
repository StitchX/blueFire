package com.blue.code;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.fire.dto", "com.blue.code", "com.fire.common.tencent","com.fire.common.config","com.fire.common.listener" })
@EnableDiscoveryClient
@EnableTransactionManagement(proxyTargetClass = true)
@EnableSwagger2Doc
@EnableScheduling
@MapperScan({"com.blue.code.mapper"})
public class ErCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ErCodeApplication.class, args);
    }

}
