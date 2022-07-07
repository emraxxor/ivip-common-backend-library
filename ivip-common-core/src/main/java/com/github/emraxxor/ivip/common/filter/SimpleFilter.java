package com.github.emraxxor.ivip.common.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class SimpleFilter<T extends Serializable> extends BaseFilter {

    private T value;

    private IdentityFilterType type = IdentityFilterType.EQUALS;

    @Override
    public void apply(PredicateFilterBuilder fb, String fieldName) {
        fb.simpleFilter(fieldName, this);
    }

    public enum IdentityFilterType {
        EQUALS,
        NOT_EQUALS,
        IS_NULL,
        IS_NOT_NULL,
        GT,
        GE,
        LT,
        LE
    }

    public SimpleFilter() {
        this.setFilterType(FilterType.IDENTITY);
    }

    public SimpleFilter(@NotNull T value) {
        this(value, IdentityFilterType.EQUALS);
    }

    public SimpleFilter(@NotNull T value, @NotNull IdentityFilterType type) {
        this();
        this.value = value;
        this.type = type;
    }


    @ToString
    public static class LongFilter extends SimpleFilter<Long> {
        public LongFilter() {
            super();
        }

        public LongFilter(@NotNull Long value) {
            super(value, IdentityFilterType.EQUALS);
        }

        public LongFilter(@NotNull Long value, @NotNull IdentityFilterType type) {
            super(value, type);
        }
    }

    @ToString
    public static class StringFilter extends SimpleFilter<String> {
        public StringFilter() {
            super();
        }

        public StringFilter(@NotNull String value) {
            super(value, IdentityFilterType.EQUALS);
        }

        public StringFilter(@NotNull String value, @NotNull IdentityFilterType type) {
            super(value, type);
        }
    }

    @ToString
    public static class IntegerFilter extends SimpleFilter<Integer> {
        public IntegerFilter() {
            super();
        }

        public IntegerFilter(@NotNull Integer value) {
            super(value, IdentityFilterType.EQUALS);
        }

        public IntegerFilter(@NotNull Integer value, @NotNull IdentityFilterType type) {
            super(value, type);
        }
    }

    @ToString
    public static class NumberFilter extends SimpleFilter<Number> {
        public NumberFilter() {
            super();
        }

        public NumberFilter(@NotNull Integer value) {
            super(value, IdentityFilterType.EQUALS);
        }

        public NumberFilter(@NotNull Integer value, @NotNull IdentityFilterType type) {
            super(value, type);
        }
    }

    @ToString
    public static class BooleanFilter extends SimpleFilter<Boolean> {
        public BooleanFilter() {
            super();
        }

        public BooleanFilter(@NotNull Boolean value) {
            super(value, IdentityFilterType.EQUALS);
        }

        public BooleanFilter(@NotNull Boolean value, @NotNull IdentityFilterType type) {
            super(value, type);
        }
    }

    @ToString
    public static class LocalDateFilter extends SimpleFilter<LocalDate> {
        public LocalDateFilter() {
            super();
        }

        public LocalDateFilter(@NotNull LocalDate value) {
            super(value);
        }

        public LocalDateFilter(@NotNull LocalDate value, @NotNull IdentityFilterType type) {
            super(value, type);
        }
    }

    @ToString
    public static class LocalDateTimeFilter extends SimpleFilter<LocalDateTime> {
        public LocalDateTimeFilter() {
            super();
        }

        public LocalDateTimeFilter(@NotNull LocalDateTime value) {
            super(value);
        }

        public LocalDateTimeFilter(@NotNull LocalDateTime value, @NotNull IdentityFilterType type) {
            super(value, type);
        }
    }
}
