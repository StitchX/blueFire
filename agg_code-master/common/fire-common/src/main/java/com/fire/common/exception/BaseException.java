package com.fire.common.exception;


import com.fire.dto.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 自定义异常类(继承RuntimeException)
 *
 * @author ZJQ 2021年5月14日14:30:26
 */
public class BaseException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;


    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private String status="500";

    public BaseException(String message) {
        super(message);
       this.message=message;
    }

    public BaseException(String message, Throwable cause) {
        super(message,cause);
        this.message = message;
    }

    public BaseException(Status status) {
        super(status.message());
        this.status = status.status();
    }

    public BaseException(String status, String message) {
        super(message);
       this.message=message;
        this.status = status;
    }

    public BaseException(String status, String message, Throwable cause) {
        super(message,cause);
        this.status = status;
        this.message=message;
    }



}