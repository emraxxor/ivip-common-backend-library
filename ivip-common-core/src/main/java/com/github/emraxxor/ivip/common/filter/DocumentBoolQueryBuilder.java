package com.github.emraxxor.ivip.common.filter;

import com.github.emraxxor.ivip.common.dto.DocumentPath;
import com.github.emraxxor.ivip.common.scroll.DefaultBoolScrollSourceResponse;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * A simple query builder for lucence based queries.
 *
 * @author Attila Barna
 */
@Builder
public class DocumentBoolQueryBuilder<BOOL_SCROLL extends DefaultBoolScrollSourceResponse<?, ?>> implements DocumentBoolQueryBuilderIF<BOOL_SCROLL> {

    private BOOL_SCROLL scroll;

    private BoolQueryBuilder query;

    @Override
    public <T extends Filter> DocumentBoolQueryBuilder<BOOL_SCROLL> with(T filter) {
        Field[] fields = filter.getClass().getDeclaredFields();
        for (Field f : fields) {
            try {
                Method m = filter.getClass().getDeclaredMethod("get" + StringUtils.capitalize(f.getName()));
                Object current = m.invoke(filter);
                processField(f, current);
            } catch (Exception e) {
                throw new IllegalStateException();
            }
        }

        if ( query.must().isEmpty() ) {
            query.must(QueryBuilders.matchAllQuery());
        }

        scroll.initQuery(query);
        return this;
    }

    @Override
    public <T> void processField(Field f, T current) {
        if (current instanceof StringFilter) {
            stringFilter(f, ((StringFilter) current));
        } else if (current instanceof RangeFilter) {
            rangeFilter(f, (RangeFilter<?>) current);
        } else if (current instanceof SimpleFilter) {
            simpleFilter(f, (SimpleFilter<?>) current);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void simpleFilter(Field field, SimpleFilter<?> filter) {
        if (filter != null && filter.getValue() != null) {
            String fieldName = getFieldName(field);

            switch (filter.getType()) {
                case EQUALS:
                    query.must().add( QueryBuilders.termQuery(fieldName,filter.getValue()) );
                    break;
                case NOT_EQUALS:
                    query.mustNot().add( QueryBuilders.termQuery(fieldName,filter.getValue()) );
                    break;
                case IS_NULL:
                    query.mustNot().add( QueryBuilders.existsQuery(fieldName) );
                    break;
                case IS_NOT_NULL:
                    query.mustNot().add( QueryBuilders.existsQuery(fieldName) );
                    break;
                case GE:
                    if ( filter instanceof SimpleFilter.NumberFilter || filter instanceof SimpleFilter.LocalDateFilter || filter instanceof SimpleFilter.LocalDateTimeFilter  ) {
                        query.must().add(QueryBuilders.rangeQuery(fieldName).gte(filter.getValue()));
                    } else {
                        throw new IllegalStateException("Not supported type (GE)");
                    }
                    break;
                case GT:
                    if ( filter instanceof SimpleFilter.NumberFilter || filter instanceof SimpleFilter.LocalDateFilter || filter instanceof SimpleFilter.LocalDateTimeFilter  ) {
                        query.must().add(QueryBuilders.rangeQuery(fieldName).gt(filter.getValue()));
                    } else {
                        throw new IllegalStateException("Not supported type (GT)");
                    }
                    break;
                case LT:
                    if ( filter instanceof SimpleFilter.NumberFilter || filter instanceof SimpleFilter.LocalDateFilter || filter instanceof SimpleFilter.LocalDateTimeFilter  ) {
                        query.must().add(QueryBuilders.rangeQuery(fieldName).lt(filter.getValue()));
                    } else {
                        throw new IllegalStateException("Not supported type (LT)");
                    }
                    break;
                case LE:
                    if ( filter instanceof SimpleFilter.NumberFilter || filter instanceof SimpleFilter.LocalDateFilter || filter instanceof SimpleFilter.LocalDateTimeFilter  ) {
                        query.must().add(QueryBuilders.rangeQuery(fieldName).lte(filter.getValue()));
                    } else {
                        throw new IllegalStateException("Not supported type (LTE)");
                    }
                    break;
                default:
                    throwFilterProcessingError(filter.getType().toString());
            }

        }
    }

    @Override
    public void rangeFilter(Field field, RangeFilter<?> current) {
        String fieldName = getFieldName(field);
        query.must().add(QueryBuilders.rangeQuery(fieldName).gte(current.getMinValue()).lte(current.getMaxValue()));
    }

    @Override
    public void stringFilter(Field field, StringFilter filter) {
        String fieldName = getFieldName(field);
        switch (filter.getType()) {
            case IGNORE_CASE_MATCH:
            case CONTAINS:
                query.must().add(QueryBuilders.matchQuery( fieldName, filter.getValue() ));
                break;
            case STARTS_WITH:
                query.must().add(QueryBuilders.prefixQuery( fieldName, filter.getValue() ));
                break;
            case EXACT_MATCH:
                query.must().add(QueryBuilders.termQuery( fieldName, filter.getValue() ));
                break;
            default:
                throwFilterProcessingError(filter.getType().toString());
        }
    }

    private String getFieldName(Field field) {
        String fieldName = "";
        if ( field.getAnnotation(DocumentPath.class) != null ) {
            fieldName += field.getAnnotation(DocumentPath.class).path() + ".";
        }
        fieldName += field.getName();
        return fieldName;
    }

    private void throwFilterProcessingError(String filterTypeName) {
        throw new IllegalArgumentException("Unable to process filter with type: " + filterTypeName);
    }
}
