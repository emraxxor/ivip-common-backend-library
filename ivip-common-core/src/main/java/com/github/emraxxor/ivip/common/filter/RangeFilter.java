package com.github.emraxxor.ivip.common.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@ToString
public class RangeFilter<C extends Comparable & Serializable> extends BaseFilter {

    private C minValue;

    private C maxValue;

    private RangeFilterType type = RangeFilterType.RANGE;

    @Override
    public void apply(PredicateFilterBuilder fb, String fieldName) {
        fb.rangeFilter(fieldName, this);
    }

    public enum RangeFilterType {
        RANGE
    }

    public RangeFilter() {
        this.setFilterType(FilterType.RANGE);
    }

    public RangeFilter(C minValue, C maxValue) {
        this();
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public static class DateRangeFilter extends RangeFilter<Instant> {
        public DateRangeFilter() {
            super();
        }

        public DateRangeFilter(Instant minValue, Instant maxValue) {
            super(minValue, maxValue);
        }
    }
}
