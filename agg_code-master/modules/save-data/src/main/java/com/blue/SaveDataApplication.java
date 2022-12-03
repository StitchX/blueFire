package com.blue;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.fire.dto", "com.blue.save", "com.fire.common"})
@EnableDiscoveryClient
@EnableTransactionManagement(proxyTargetClass = true)
@EnableSwagger2Doc
@EnableScheduling
public class SaveDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaveDataApplication.class, args);
    }

}
