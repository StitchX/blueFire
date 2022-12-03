package com.blue.business.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.blue.business.security.util.PreSecurityUser;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author : [Cohen]
 * @version : [v1.0]
 * @createTime : [2022/3/21 16:11]
 */

@Configuration
public class MybatisPlusConfig {
/**
     * 多租户配置
     * @return
     */

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {

        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                PreSecurityUser loginUser = (PreSecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//                PreSecurityUser loginUser = new PreSecurityUser("15828371013",96L);
                return new LongValue(loginUser.getMerchantId());
            }

            @Override
            public boolean ignoreTable(String tableName) {
                return "merchant_user".equalsIgnoreCase(tableName) || "address_info".equalsIgnoreCase(tableName)
                        || "bank".equalsIgnoreCase(tableName) || "category".equalsIgnoreCase(tableName)
                        || "notice_info".equalsIgnoreCase(tableName) || "notice_details_info".equalsIgnoreCase(tableName)
                        || "employment_certificate".equalsIgnoreCase(tableName) || "health_certificate".equalsIgnoreCase(tableName);
            }

            @Override
            public String getTenantIdColumn() {
                return "merchant_id";
            }
        }));
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        //注册乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }
}
