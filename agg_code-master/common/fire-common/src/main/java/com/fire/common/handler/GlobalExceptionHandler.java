package com.fire.common.handler;


import com.fire.common.exception.BaseException;
import com.fire.dto.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

import static com.fire.dto.enums.Status.FAILURE;

/**
 * 异常处理类
 *
 * @author ZJQ 2021年5月14日14:32:00
 */
@ControllerAdvice("com.fire")
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理自定义基础异常
     */
    @ExceptionHandler(BaseException.class)
    public BaseResponse resExceptionHandler(HttpServletResponse response, BaseException ex) {
        response.setStatus(501);
        return new BaseResponse(ex.getStatus(), ex.getMessage());
    }

    /**
     * 处理未知异常
     */
    @ExceptionHandler(Exception.class)
    public BaseResponse otherExceptionHandler(HttpServletResponse response, Exception ex) {
        response.setStatus(500);
        log.error(FAILURE.message(), ex);
        return new BaseResponse(FAILURE.status(), ex.getMessage());
    }

}
