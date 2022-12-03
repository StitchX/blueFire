package com.blue.business.exception;

import com.fire.common.exception.BaseException;
import com.fire.dto.response.BaseRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Description: 全局异常
 * @ClassName: BusinessExecption
 * @Author: liuliu
 * @Date: 2022/3/23 15:48
 */
@Slf4j
@RestControllerAdvice(annotations = {Controller.class, RestController.class})
public class BusinessExecption {

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(BaseException.class)
    public BaseRestResponse handlerException(BaseException e) {
        return BaseRestResponse.error(e.getStatus(), e.getMessage());
    }

    @ExceptionHandler(ValidateCodeException.class)
    public BaseRestResponse handleValidateCodeException(ValidateCodeException e) {
        log.error(e.getMessage(), e);
        return BaseRestResponse.error(e.getMessage());
    }

}
