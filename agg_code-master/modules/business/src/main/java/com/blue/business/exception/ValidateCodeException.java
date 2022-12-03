package com.blue.business.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @Description:
 * @ClassName: ValidateCodeException
 * @Author: liuliu
 * @Date: 2022/3/21 15:38
 */
public class ValidateCodeException extends AuthenticationException {

    private static final long serialVersionUID = 5022575393500654459L;

    public ValidateCodeException(String message) {
        super(message);
    }
}
