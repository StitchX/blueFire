package com.fire.api.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.fire.dto.enums.DataSourceNames;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 配置多数据源
 * @ClassName: DynamicDataSourceConfig
 * @Author: liuliu
 * @Date: 2021/10/19 12:01
 */
@Configuration
@Slf4j
@AllArgsConstructor
public class DynamicDataSourceConfig {


    @Bean
    @ConfigurationProperties("spring.datasource.master")
    public DataSource masterDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.slave")
    public DataSource slaveDataSource() {
        return DruidDataSourceBuilder.create().build();
    }


    @Bean
    @Primary
    public DynamicDataSource dataSource() {
        Map<Object, Object> targetDataSources = new HashMap<>(5);
        targetDataSources.put(DataSourceNames.MASTER.getKey(), masterDataSource());
        targetDataSources.put(DataSourceNames.SLAVE.getKey(), slaveDataSource());
        DynamicDataSource dynamicDataSource = new DynamicDataSource(masterDataSource(), targetDataSources);
        DruidDataSource masterDruidDataSource = (DruidDataSource) targetDataSources.get(DataSourceNames.MASTER.getKey());
        DruidDataSource slaveDruidDataSource = (DruidDataSource) targetDataSources.get(DataSourceNames.SLAVE.getKey());
        log.debug("datasource connections masterurl: 【{}】，slaveurl:【{}】", masterDruidDataSource.getUrl(), slaveDruidDataSource.getUrl());
        return dynamicDataSource;
    }

}
