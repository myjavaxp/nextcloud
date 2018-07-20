package com.yibo.exception;

import com.yibo.entity.common.Status;
import org.springframework.http.HttpStatus;

public class CommonException extends RuntimeException {
    private static final long serialVersionUID = 3965781030087386799L;
    private Integer code;

    public CommonException(HttpStatus httpStatus) {
        super(httpStatus.getReasonPhrase());
        this.code = httpStatus.value();
    }

    public CommonException(Status status) {
        super(status.getReasonPhrase());
        this.code = status.value();
    }

    public CommonException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}