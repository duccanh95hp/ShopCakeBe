package com.example.shop_cake_be.common;

import org.springframework.http.HttpStatus;

public class WebServiceException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    private int httpStatus = HttpStatus.REQUEST_TIMEOUT.value();
    private int code = 99;

    public WebServiceException(int httpStatus, int code, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.code = code;
    }

    public WebServiceException(int httpStatus, String message, Object errorData) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public WebServiceException(int httpStatus, int code, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.code = code;
    }

    public WebServiceException(int httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public WebServiceException(int httpStatus, Throwable cause) {
        super(cause);
        this.httpStatus = httpStatus;
    }

    public WebServiceException(int httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public WebServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebServiceException(Throwable cause) {
        super(cause);
    }

    public WebServiceException(String message) {
        super(message);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public WebServiceException setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

    public int getCode() {
        return code;
    }

    public WebServiceException setCode(int code) {
        this.code = code;
        return this;
    }
}

