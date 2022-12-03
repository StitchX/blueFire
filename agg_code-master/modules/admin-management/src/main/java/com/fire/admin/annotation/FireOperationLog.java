package com.fire.admin.annotation;

import java.lang.annotation.*;

/**
 * @author admin
 * 操作日志注解
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FireOperationLog {

    String description() default "";

    String module() default "";
}
