package com.fire.api.config;


import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author liuliu
 * @version V1.0
 * @ClassName: MybatisPlusConfig
 * @Function: MybatisPlus 插件配置
 * @Date: 2020/8/14 14:24
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * @param
     * @return PaginationInterceptor
     * @Description MybatisPlus 分页插件配置
     * @date: 2020/8/14 15:35
     * @Author liuliu
     * @Version V1.0
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }


    /**
     * @param
     * @return cn.hutool.core.lang.Snowflake
     * @Description 分布式id生成
     * @date: 2020/8/14 15:35
     * @Author liuliu
     * @Version V1.0
     */
    @Bean
    public Snowflake snowflake() {
        return IdUtil.createSnowflake(1, 1);
    }

    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }


    /**
     * @descible: 数据权限
     * @param: dataSource
     * @return: com.fire.admin.util.DataScopeInterceptor
     * @author: liuliu
     * @date: 2021-06-29 11:50
     */
/*    @Bean
    @ConditionalOnMissingBean
    public DataScopeInterceptor dataScopeInterceptor(DataSource dataSource) {
        return new DataScopeInterceptor(dataSource);
    }*/

}