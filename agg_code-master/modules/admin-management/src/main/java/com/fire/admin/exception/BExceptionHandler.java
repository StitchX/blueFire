package com.fire.admin.exception;


import com.fire.common.exception.BaseException;
import com.fire.dto.response.BaseRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import javax.security.auth.login.AccountExpiredException;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;

/**
 * @Classname BExceptionHandler
 * @Description 自定义异常处理
 * @Author 李号东 lihaodongmail@163.com
 * @Date 2019-03-29 13:23
 * @Version 1.0
 */
@Slf4j
@RestControllerAdvice(annotations = {Controller.class, RestController.class})
public class BExceptionHandler {


    /**
     * 处理自定义异常
     */
    @ExceptionHandler(BaseException.class)
    public BaseRestResponse handlerException(BaseException e) {
        return BaseRestResponse.error(e.getStatus(), e.getMessage());
    }

    @ExceptionHandler(MultipartException.class)
    public BaseRestResponse uploadExcepttion(MultipartException e){
        return BaseRestResponse.error("超出文件最大上传限制");
    }



    @ExceptionHandler(DuplicateKeyException.class)
    public BaseRestResponse handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        return BaseRestResponse.error("300", "数据库中已存在该记录");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public BaseRestResponse handleAuthorizationException(AccessDeniedException e) {
        log.error(e.getMessage());
        return BaseRestResponse.error("403", "没有权限，请联系管理员授权");
    }

    @ExceptionHandler(AccountExpiredException.class)
    public BaseRestResponse handleAccountExpiredException(AccountExpiredException e) {
        log.error(e.getMessage(), e);
        return BaseRestResponse.error(e.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public BaseRestResponse handleUsernameNotFoundException(UsernameNotFoundException e) {
        log.error(e.getMessage(), e);
        return BaseRestResponse.error(e.getMessage());
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public BaseRestResponse handleUsernameNotFoundException(InternalAuthenticationServiceException e) {
        log.error(e.getMessage(), e);
        return BaseRestResponse.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public BaseRestResponse handleException(Exception e) {
        log.error(e.getMessage(), e);
        return BaseRestResponse.error(e.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    public BaseRestResponse handlerSqlException(SQLException e) {
        log.error(e.getMessage(), e);
        return BaseRestResponse.error(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public BaseRestResponse handlerIllegalArgumentException(IllegalArgumentException e) {
        return BaseRestResponse.error("文件长度不能小于3");
    }


    @ExceptionHandler(ValidateCodeException.class)
    public BaseRestResponse handleValidateCodeException(ValidateCodeException e) {
        log.error(e.getMessage(), e);
        return BaseRestResponse.error(e.getMessage());
    }

}
