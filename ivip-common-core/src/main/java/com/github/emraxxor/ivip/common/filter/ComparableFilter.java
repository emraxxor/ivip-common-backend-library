package com.github.emraxxor.ivip.common.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *
 * The comparison filter is suitable for comparing values.
 *
 * @param <C>
 *
 * @author Attila Barna
 */
@Getter
@Setter
@ToString
public class ComparableFilter<C extends Comparable & Serializable> extends BaseFilter {

    private C value;

    private ComparableFilterType type = ComparableFilterType.EQUALS;

    @Override
    public void apply(PredicateFilterBuilder fb, String fieldName) {
        fb.comparableFilter(fieldName, this);
    }

    public enum ComparableFilterType {
        EQUALS,
        NOT_EQUALS,
        GREATER_THAN,
        GREATER_THAN_OR_EQUALS,
        LESS_THAN,
        LESS_THAN_OR_EQUALS
    }

    public ComparableFilter() {
        this.setFilterType(FilterType.COMPARABLE);
    }

    public ComparableFilter(@NotNull C value, @NotNull ComparableFilterType type) {
        this();
        this.value = value;
        this.type = type;
    }
}
