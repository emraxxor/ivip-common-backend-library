package com.github.emraxxor.ivip.common.scroll;

import com.github.emraxxor.ivip.common.es.ScrollSourceData;
import com.github.emraxxor.ivip.common.response.Response;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * Simple interface for scroll based queries
 *
 * @param <DATA_TYPE>
 *
 * @author Attila Barna
 *
 * @collection ivip.cloud
 */
public interface ScrollSource<DATA_TYPE, SCROLL_DATA extends ScrollSourceData> extends Response {


    /**
     * Specifies the query
     */
    void query();

    /**
     * Application's context
     * @param context
     */
    void context(ApplicationContext context);

    /**
     * Current token
     */
    SCROLL_DATA scroll();


    /**
     * Number of documents
     */
    int count();

    /**
     * Query initialization
     */
    void initializeQuery();

    /**
     * Size of the response
     * @return
     */
    int size();

    /**
     * Total result number of the query
     */
    long total();

    /**
     * The result of the query
     */
    List<DATA_TYPE> content();

    /**
     * Sets extra parameters for the source
     */
    void params(List<?> params);


    default void postInit() {}


    default void beforeDestroy() {}
}
