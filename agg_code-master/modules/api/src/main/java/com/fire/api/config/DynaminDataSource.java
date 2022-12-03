package com.fire.api.config;


import java.lang.annotation.*;


/**
 * @Description: 多数据源注解
 * @ClassName: DataSourceAspect
 * @Author: liuliu
 * @Date: 2021/10/19 11:45
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynaminDataSource {

    String[] names() default {};
}
