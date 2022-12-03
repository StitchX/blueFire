package com.fire.admin.aop;

import cn.hutool.extra.servlet.ServletUtil;
import com.fire.admin.annotation.FireOperationLog;
import com.fire.admin.service.LogInfoService;
import com.fire.admin.util.DateUtil;
import com.fire.admin.util.SecurityUtil;
import com.fire.dto.log.LogInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;


/**
 * @author admin
 * 操作日志记录切面
 */
@Aspect
@Component
@Slf4j
public class FireOperationLogAop {

    @Resource
    private HttpServletRequest request;

    @Resource
    private LogInfoService logInfoService;

    @Pointcut("@annotation(com.fire.admin.annotation.FireOperationLog)")
    public void operationLog() {
    }

    @Around("operationLog()")
    public Object getReturning(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取swagger接口说明注解
        MethodSignature methodSign = (MethodSignature) joinPoint.getSignature();
        Method method = methodSign.getMethod();
        StringBuilder desc = new StringBuilder();
        Object obj = null;

        FireOperationLog fireOperationLog = method.getAnnotation(FireOperationLog.class);
        String username = SecurityUtil.getUser().getUsername();

        if (method.isAnnotationPresent(FireOperationLog.class)) {
            if (StringUtils.isNotEmpty(fireOperationLog.description())) {
                desc = new StringBuilder(fireOperationLog.description());
            } else {
                // 判断是否有ApiOperation注解
                boolean flag = method.isAnnotationPresent(ApiOperation.class);
                if (flag) {
                    ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
                    desc = new StringBuilder(apiOperation.value());
                }
            }

            try {
                obj = joinPoint.proceed();
                desc.append("成功");
            } catch (Throwable throwable) {
                desc.append("失败");
                throw throwable;
            } finally {

                LogInfo logInfo = LogInfo.builder()
                        //.supervisionId(SecurityUtil.getUser().getSupervisionId())
                        .name(username)
                        .content(username.concat("在" + DateUtil.formatLocalDateTime(LocalDateTime.now())).concat(desc.toString()))
                        .ip(ServletUtil.getClientIPByHeader(request, "X-Forwarded-For"))
                        .operateTime(new Timestamp(System.currentTimeMillis()))
                        .module(method.getAnnotation(FireOperationLog.class).module())
                        .params(Arrays.toString(joinPoint.getArgs()))
                        .build();

                logInfoService.saveLogInfo(logInfo);

            }
        }
        return obj;
    }
}
