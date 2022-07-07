package com.github.emraxxor.ivip.common.es.manager;

import com.github.emraxxor.ivip.common.es.AbstractBaseDataElement;
import com.github.emraxxor.ivip.common.es.ESDataElement;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * "DocumentManager" is a simple component for retrieving stored documents from elasticsearch.
 *
 * @author Attila Barna
 */
@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class DefaultDocumentManager implements DocumentManager{

    private final RestHighLevelClient restHighLevelClient;

    @Override
    public <T extends AbstractBaseDataElement> Optional<T> findByDocumentId(String id, String index, Type type) {
        try {
            GetRequest request = new GetRequest(index, id);
            request.fetchSourceContext(FetchSourceContext.FETCH_SOURCE);
            GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
            if ( response.isExists() ) {
                T data = new Gson().fromJson(response.getSourceAsString(), type);

                if (data != null)
                    (( ESDataElement )data).documentId(response.getId());

                assert data != null;
                return Optional.of(data);
            }
        } catch (JsonSyntaxException | IOException e ) {
            log.error(e.getMessage(),e);
        }
        return Optional.empty();
    }

    private SearchRequest createBoolQuery(List<Field> fields, IndexMetaData metaData) {
        BoolQueryBuilder query = QueryBuilders.boolQuery();

        for(Field field : fields )
            query.must(QueryBuilders.termQuery(field.getFieldName(), field.getFieldValue()));

        SearchRequest sr = new SearchRequest(metaData.getIndexName());
        SearchSourceBuilder src = new SearchSourceBuilder();
        src.query(query);
        src.size(1);
        src.explain(false);
        sr.source(src);

        if ( metaData.getRouting() != null )
            sr.routing(metaData.getRouting());

        return sr;
    }

    public <T extends AbstractBaseDataElement> Optional<T> findByField(List<Field> fields, IndexMetaData metaData, Type type) {
        try {
            SearchResponse response = restHighLevelClient.search(createBoolQuery(fields,metaData), RequestOptions.DEFAULT);

            GsonBuilder gsonBuilder = new GsonBuilder();

            if ( !metaData.exclusionStrategies().isEmpty() )
                metaData.exclusionStrategies().forEach(gsonBuilder::addDeserializationExclusionStrategy);

            Gson gson = gsonBuilder.create();

            return Arrays.stream(response.getHits().getHits())
                    .map(e -> (T) gson.fromJson(e.getSourceAsString(), type) )
                    .filter(Objects::nonNull)
                    .peek(e -> {
                        if (metaData.getRouting() != null) {
                            e.routing(metaData.getRouting());
                        }
                    })
                    .findAny();
        } catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    public <T extends AbstractBaseDataElement> Optional<T> findByIdUsingMeta(String id, IndexMetaData meta, Type type ) {

        try {
            GetRequest grq = new GetRequest(meta.getIndexName());
            grq.id(id);
            grq.fetchSourceContext(FetchSourceContext.FETCH_SOURCE);

            if ( meta.getRouting() != null )
                grq.routing(meta.getRouting());

            GetResponse gr = restHighLevelClient.get(grq, RequestOptions.DEFAULT) ;
            GsonBuilder gsonBuilder = new GsonBuilder();

            if ( !meta.exclusionStrategies().isEmpty() )
                meta.exclusionStrategies().forEach(gsonBuilder::addDeserializationExclusionStrategy);

            Gson gson = gsonBuilder.create();

            T o = gson.fromJson(gr.getSourceAsString(), type );


            if (o != null) {
                o.documentId(gr.getId());
            }

            if ( o != null &&  meta.getRouting() != null )
                o.routing(meta.getRouting());


            assert o != null;
            return Optional.of(o);
        } catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public Long countByField(List<Field> fields, IndexMetaData metaData) {
        try {
            SearchResponse response = restHighLevelClient.search(createBoolQuery(fields,metaData), RequestOptions.DEFAULT);
            return response.getHits().getTotalHits().value;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1L;
    }

    public <T extends AbstractBaseDataElement> List<T> findByFieldList(List<Field> fields, IndexMetaData metaData, Type type, int size, int from) {
        final BoolQueryBuilder query = QueryBuilders.boolQuery();
        final SearchRequest rq = new SearchRequest(metaData.getIndexName());
        final SearchSourceBuilder ssb = new SearchSourceBuilder();

        for(Field field : fields )
            query.must(QueryBuilders.termQuery(field.getFieldName(), field.getFieldValue()));

        ssb.query(query);
        ssb.size(size);

        rq.source(ssb);

        if ( metaData.getRouting() != null )
            rq.routing(metaData.getRouting());

        try {
            SearchResponse response = restHighLevelClient.search(rq, RequestOptions.DEFAULT);

            GsonBuilder gsonBuilder = new GsonBuilder();

            if ( !metaData.exclusionStrategies().isEmpty() )
                metaData.exclusionStrategies().forEach(gsonBuilder::addDeserializationExclusionStrategy);

            Gson gson = gsonBuilder.create();

            return Arrays
                    .stream(response.getHits().getHits())
                    .map(e -> (T) gson.fromJson(response.getHits().getHits()[0].getSourceAsString(), type))
                    .peek((data) -> {
                        if (data != null) {
                            (( ESDataElement )data).documentId(response.getHits().getHits()[0].getId());

                            if (metaData.getRouting() != null)
                                data.routing(metaData.getRouting());
                        }
                    }).collect(Collectors.toList());

        } catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException();
    }

}
