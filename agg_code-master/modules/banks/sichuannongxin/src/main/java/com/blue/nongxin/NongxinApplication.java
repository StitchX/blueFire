package com.blue.nongxin;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.fire.dto", "com.blue.nongxin", "com.fire.common"}, exclude = {DataSourceAutoConfiguration.class})
@MapperScan({"com.blue.nongxin.mapper"})
@EnableDiscoveryClient
@EnableTransactionManagement(proxyTargetClass = true)
@EnableSwagger2Doc
@EnableScheduling
public class NongxinApplication {

    public static void main(String[] args) {
        SpringApplication.run(NongxinApplication.class, args);
    }

}
