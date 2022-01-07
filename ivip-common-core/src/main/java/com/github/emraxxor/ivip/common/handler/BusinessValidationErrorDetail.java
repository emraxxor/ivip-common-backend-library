package com.github.emraxxor.ivip.common.handler;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@ToString
public class BusinessValidationErrorDetail implements Serializable {

    private String message;

    private String field;

    private String rejectedValue;

    private Map<String, String> additionalInfo;

    @JsonCreator
    public BusinessValidationErrorDetail(@JsonProperty("message") String message,
                                         @JsonProperty("field") String field,
                                         @JsonProperty("rejectedValue") String rejectedValue) {
        this.message = message;
        this.field = field;
        this.rejectedValue = rejectedValue;
    }

    @JsonCreator
    public BusinessValidationErrorDetail(String message,
                                         String field,
                                         String rejectedValue,
                                         Map<String,String> additionalInfo
                                         ) {
        this.message = message;
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.additionalInfo = additionalInfo;
    }
}
