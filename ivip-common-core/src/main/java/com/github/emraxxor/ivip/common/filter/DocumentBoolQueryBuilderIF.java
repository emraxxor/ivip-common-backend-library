package com.github.emraxxor.ivip.common.filter;

import com.github.emraxxor.ivip.common.scroll.DefaultBoolScrollSourceResponse;

import java.lang.reflect.Field;

/**
 * A simple query builder for lucence based queries.
 * @param <SCROLL>
 * @author Attila Barna
 */
public interface DocumentBoolQueryBuilderIF<SCROLL extends DefaultBoolScrollSourceResponse<?, ?>> {

    <T extends Filter> DocumentBoolQueryBuilderIF<SCROLL> with(T filter);

    <T> void processField(Field f, T current);

    void simpleFilter(Field field, SimpleFilter<?> current);

    void rangeFilter(Field field, RangeFilter<?> current);

    void stringFilter(Field field, StringFilter filter);
}
