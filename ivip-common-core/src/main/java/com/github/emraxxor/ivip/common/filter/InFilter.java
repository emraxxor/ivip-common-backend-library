package com.github.emraxxor.ivip.common.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
public class InFilter<T extends Serializable> extends BaseFilter {

    protected List<T> value;

    protected InFilterType type = InFilterType.IN;

    @Override
    public void apply(PredicateFilterBuilder fb, String fieldName) {
        fb.inFilter(fieldName, this);
    }

    public enum InFilterType {
        IN
    }

    public InFilter() {
        this.setFilterType(FilterType.IN);
    }

    public InFilter(@NotEmpty List<T> value) {
        this();
        this.value = value;
    }

    @ToString
    public static class UUIDFilter extends InFilter<UUID> {
        public UUIDFilter() {
            super();
        }

        public UUIDFilter(@NotEmpty List<UUID> valueList) {
            super(valueList);
        }
    }

    @ToString
    public static class LongFilter extends InFilter<Long> {
        public LongFilter() {
            super();
        }

        public LongFilter(@NotEmpty List<Long> valueList) {
            super(valueList);
        }
    }

    @ToString
    public static class StringFilter extends InFilter<String> {
        public StringFilter() {
            super();
        }

        public StringFilter(@NotEmpty List<String> valueList) {
            super(valueList);
        }
    }

    @ToString
    public static class LocalDateFilter extends InFilter<LocalDate> {
        public LocalDateFilter() {
            super();
        }

        public LocalDateFilter(@NotEmpty List<LocalDate> valueList) {
            super(valueList);
        }
    }

    @ToString
    public static class LocalDateTimeFilter extends InFilter<LocalDateTime> {
        public LocalDateTimeFilter() {
            super();
        }

        public LocalDateTimeFilter(@NotEmpty List<LocalDateTime> valueList) {
            super(valueList);
        }
    }

    @ToString
    public static class IntegerFilter extends InFilter<Integer>{
        public IntegerFilter() {
            super();
        }

        public IntegerFilter(@NotEmpty List<Integer> valueList) { super(valueList);}
    }

}
