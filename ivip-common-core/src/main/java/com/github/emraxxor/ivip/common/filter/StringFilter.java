package com.github.emraxxor.ivip.common.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class StringFilter extends BaseFilter {

    private String value;

    private StringFilterType type = StringFilterType.CONTAINS;

    @Override
    public void apply(PredicateFilterBuilder fb, String fieldName) {
        fb.stringFilter(fieldName, this);
    }

    public enum StringFilterType {
        CONTAINS,
        STARTS_WITH,
        EXACT_MATCH,
        IGNORE_CASE_MATCH
    }

    public StringFilter() {
        this.setFilterType(FilterType.STRING);
    }

    public StringFilter(@NotNull String value) {
        this(value, StringFilterType.CONTAINS);
    }

    public StringFilter(@NotNull String value, @NotNull StringFilterType type) {
        this();
        this.value = value;
        this.type = type;
    }
}
