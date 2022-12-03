package com.fire.api.config;

import cn.hutool.core.util.RandomUtil;
import com.fire.dto.enums.DataSourceNames;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Description: 多数据源切面处理类
 * @ClassName: DataSourceAspect
 * @Author: liuliu
 * @Date: 2021/10/19 11:45
 */
@Slf4j
@Aspect
@Component
public class DataSourceAspect implements Ordered {

    @Pointcut("@annotation(com.fire.api.config.DynaminDataSource)")
    public void dataSourcePointCut() {

    }


    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        DynaminDataSource ds = method.getAnnotation(DynaminDataSource.class);
        if (ds == null) {
            DynamicDataSource.setDataSource(DataSourceNames.MASTER.getKey());
            log.debug("set datasource is " + DataSourceNames.MASTER.getKey());
        } else {
            String[] names = ds.names();
            String target = DataSourceNames.MASTER.getKey();
            if (1 == names.length) {
                target = names[0];
                DynamicDataSource.setDataSource(target);
            } else if (2 == names.length) {
                int randomInt = RandomUtil.randomInt(0, 2);
                target = names[randomInt];
                DynamicDataSource.setDataSource(target);
            } else {
                DynamicDataSource.setDataSource(target);
            }
            log.debug("set datasource is " + target);
        }

        try {
            return point.proceed();
        } finally {
            String dataSource = DynamicDataSource.getDataSource();
            DynamicDataSource.clearDataSource();
            log.debug("clean datasource is :[{}]",dataSource);
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
