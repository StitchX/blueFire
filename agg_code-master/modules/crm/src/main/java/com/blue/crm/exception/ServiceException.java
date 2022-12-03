package com.blue.crm.exception;

import lombok.Data;

@Data
public class ServiceException extends RuntimeException {
    private int code;
    private String message;

    public ServiceException(int code, String message) {
        super(code + "" + message);
        this.code = code;
        this.message = message;
    }
}
