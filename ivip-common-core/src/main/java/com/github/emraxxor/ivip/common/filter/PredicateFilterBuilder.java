package com.github.emraxxor.ivip.common.filter;

import java.io.Serializable;

/**
 * @author Attila Barna
 */
public interface PredicateFilterBuilder {

    /**
     * Create a predicate for testing whether the expression satisfies the pattern of the given filter.
     *
     * @param entityFieldName
     * @param filter
     * @return
     */
    PredicateFilterBuilder stringFilter(String entityFieldName, StringFilter filter);

    /**
     * Create a simple filter for comparing data elements
     *
     * @param entityFieldName
     * @param filter
     * @param <U>
     * @return
     */
    <U extends Serializable> PredicateFilterBuilder simpleFilter(String entityFieldName, SimpleFilter<U> filter);

    /**
     * Create a predicate for testing whether the first argument is between the second and third arguments in value.
     *
     * @param entityFieldName
     * @param filter
     * @param <C>
     * @return
     */
    <C extends Comparable & Serializable> PredicateFilterBuilder rangeFilter(String entityFieldName, RangeFilter<C> filter);

    /**
     * Create a predicate for testing the comparable arguments
     * .
     * @param entityFieldName
     * @param filter
     * @param <C>
     * @return
     */
    <C extends Comparable & Serializable> PredicateFilterBuilder comparableFilter(String entityFieldName, ComparableFilter<C> filter);

    /**
     * Create predicate to test whether given expression is contained in a list of values.
     *
     * @param entityFieldName
     * @param filter
     * @param <U>
     * @return
     */
    <U extends Serializable> PredicateFilterBuilder inFilter(String entityFieldName, InFilter<U> filter);

}
