package com.github.emraxxor.ivip.common.scroll;

import com.github.emraxxor.ivip.common.es.AbstractBaseDataElement;
import com.github.emraxxor.ivip.common.es.DefaultScrollSourceData;
import com.github.emraxxor.ivip.common.filter.DocumentBoolScrollQueryIF;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.util.function.Function;

/**
 * Default implementation for @{@link com.github.emraxxor.ivip.common.filter.DocumentBoolQueryBuilder}
 *
 * @param <T>
 * @param <SCROLL_DATA>
 * @author Attila Barna
 */
public class DefaultBoolScrollSourceResponse<
        T extends AbstractBaseDataElement,
        SCROLL_DATA extends DefaultScrollSourceData
        > extends AbstractBoolScrollSourceResponse<T, SCROLL_DATA> implements DocumentBoolScrollQueryIF {

    private final Function<SearchHit, T> transformer;

    protected DefaultBoolScrollSourceResponse(String index, SCROLL_DATA scrollSourceData, Function<SearchHit, T> transformer) {
        super(index, scrollSourceData);
        this.transformer = transformer;
    }

    public static <
            T extends AbstractBaseDataElement,
            S extends DefaultScrollSourceData>
    DefaultBoolScrollSourceResponse<T, S>
    create(String index, S scroll, Function<SearchHit, T> transformer) {
        return new DefaultBoolScrollSourceResponse<>(index, scroll, transformer);
    }

    @Override
    public void initializeQuery() {
        if (this.query == null) {
            query = QueryBuilders.boolQuery().must(QueryBuilders.matchAllQuery());
        }
    }

    @Override
    public T convert(SearchHit o) {
        return transformer.apply(o);
    }

    @Override
    public void initQuery(BoolQueryBuilder qb) {
        if (qb != null) {
            this.query = qb;
        }
    }
}
