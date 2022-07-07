package com.github.emraxxor.ivip.common.scroll;

import com.github.emraxxor.ivip.common.es.ScrollSourceData;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 *
 * @param <DATA_TYPE>
 *
 * @author Attila Barna
 */
public interface ScrollOperations<DATA_TYPE> {

    /**
     * Post initialization
     */
    void postInit();

    /**
     * Before destroy
     */
    void beforeDestroy();

    /**
     * Initialize the query of the source
     */
    void queryInitialization();

    /**
     * Execute the query
     */
    void executeQuery();

    /**
     * Applications context
     * @param context
     */
    void context(ApplicationContext context);

    /**
     * Called after the query has been executed
     */
    void onQueryComplete();

    /**
     * Sets the source
     * @param source
     */
    <E extends ScrollSource<DATA_TYPE, ScrollSourceData>> void source(E source);

    /**
     * Sets the parameters
     * @param params
     */
    void params(List<?> params);
}
