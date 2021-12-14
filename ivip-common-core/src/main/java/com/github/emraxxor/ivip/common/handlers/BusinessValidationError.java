package com.github.emraxxor.ivip.common.handlers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Getter
@ToString
public class BusinessValidationError implements Serializable {

    private final Instant timestamp;

    private final int status;

    private final String error;

    private final String path;

    private final List<BusinessValidationErrorDetail> errors;


    @JsonCreator
    public BusinessValidationError(@JsonProperty("timestamp") Instant timestamp,
                                   @JsonProperty("status") int status,
                                   @JsonProperty("error") String error,
                                   @JsonProperty("path") String path,
                                   @JsonProperty("errors") List<BusinessValidationErrorDetail> errors) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.path = path;
        this.errors = errors;
    }

}
