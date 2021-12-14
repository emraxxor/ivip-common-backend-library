package com.github.emraxxor.ivip.common.exception;

public class RequestValidationException extends ValidationException {

    public RequestValidationException() {
    }

    public RequestValidationException(String message) {
        super(message);
    }

    public RequestValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestValidationException(Throwable cause) {
        super(cause);
    }

    public RequestValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}