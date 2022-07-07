package com.github.emraxxor.ivip.common.scroll;

import com.github.emraxxor.ivip.common.es.AbstractBaseDataElement;
import com.github.emraxxor.ivip.common.es.DefaultScrollSourceData;
import com.github.emraxxor.ivip.common.es.ESDataElement;
import com.google.gson.reflect.TypeToken;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * Simple component that is suitable for creating Boolean based queries
 *
 * @param <T>
 *
 * @author Attila Barna
 * @collection ivip.cloud
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AbstractBoolScrollSourceResponse<
                        T extends AbstractBaseDataElement,
                        SCROLL_DATA extends DefaultScrollSourceData
                      > extends AbstractScrollResponse<T, SCROLL_DATA> {

    transient RestHighLevelClient client;

    transient SearchResponse response;

    transient BoolQueryBuilder query;

    final String index;

    transient ApplicationContext context;

    protected AbstractBoolScrollSourceResponse(String index, SCROLL_DATA scrollSourceData) {
        super(scrollSourceData);
        this.index = index;
    }

    @Override
    public void context(ApplicationContext context) {
        this.context = context;
        client = context.getBean(RestHighLevelClient.class);
    }

    @Override
    public void query()  {
        try {
            if (scroll.token() == null || scroll.token().equalsIgnoreCase("") || scroll.token().equalsIgnoreCase("null"))  {
                SearchRequest searchRequest = new SearchRequest(index);
                SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
                sourceBuilder.query(query);
                sourceBuilder.size(scroll.size());
                searchRequest.scroll(new TimeValue(30000L));
                searchRequest.source(sourceBuilder);
                response = client.search(searchRequest, RequestOptions.DEFAULT);
            } else {
                SearchScrollRequest scrollRequest = new SearchScrollRequest(scroll.token());
                scrollRequest.scroll(new TimeValue(30000L));
                response = client.scroll(scrollRequest, RequestOptions.DEFAULT);
            }
            scroll.setToken( response.getScrollId() );
            total = response.getHits().getTotalHits().value;
            count = response.getHits().getHits().length;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int count() {
        return this.count;
    }


    public T convert(SearchHit o) {
        T e = gson().fromJson(o.getSourceAsString(), new TypeToken<T>(){}.getType());
        if (e != null)
            ((ESDataElement)e).documentId(o.getId());
        return e;
    }

    @Override
    public List<T> content() {
        return Arrays.stream(response.getHits().getHits())
                    .map(this::convert)
                    .collect(Collectors.toList());
    }
}
