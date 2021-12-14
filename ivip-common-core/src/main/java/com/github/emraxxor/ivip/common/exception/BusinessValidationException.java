package com.github.emraxxor.ivip.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessValidationException extends ValidationException {

    private final HttpStatus status;

    private final int statusCode;

    private final String statusMessage;

    private final String field;

    private final String rejectedValue;

    public BusinessValidationException(String message) {
        this(message, null, null);
    }

    public BusinessValidationException(String message, String field) {
        this(message, field, null);
    }

    public BusinessValidationException(String message, String field, Object rejectedValue) {
        super(message);

        if (rejectedValue != null) {
            this.rejectedValue = rejectedValue.toString();
        } else {
            this.rejectedValue = null;
        }

        this.field = field;
        this.status = HttpStatus.BAD_REQUEST;
        this.statusCode = HttpStatus.BAD_REQUEST.value();
        this.statusMessage = HttpStatus.BAD_REQUEST.toString();
    }

}
