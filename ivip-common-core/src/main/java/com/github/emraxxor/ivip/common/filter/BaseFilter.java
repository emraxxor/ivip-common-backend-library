package com.github.emraxxor.ivip.common.filter;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.emraxxor.ivip.common.request.Request;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "filterType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StringFilter.class, name = "STRING"),
        @JsonSubTypes.Type(value = RangeFilter.class, name = "RANGE"),
        @JsonSubTypes.Type(value = ComparableFilter.class, name = "COMPARABLE"),
        @JsonSubTypes.Type(value = InFilter.class, name = "IN"),
        @JsonSubTypes.Type(value = SimpleFilter.class, name = "SIMPLE"),
})
@Getter
@Setter
public abstract class BaseFilter implements Request {

    @ApiModelProperty(hidden = true)
    private FilterType filterType;

    public abstract void apply(PredicateFilterBuilder fb, String fieldName);

}
