package com.github.emraxxor.ivip.common.scroll;

import com.github.emraxxor.ivip.common.es.ScrollSourceData;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 *
 * Default scroll response for IScroll component
 * @author Attila Barna
 *
 */
public class DefaultScrollResponse<DATA_TYPE>
        extends DefaultScrollResponseData<DATA_TYPE, ScrollSourceData>
        implements ScrollOperations<DATA_TYPE> {

    protected ScrollSource<DATA_TYPE, ScrollSourceData> source;

    public DefaultScrollResponse() {
        super();
    }

    @Override
    public void queryInitialization() {
        source.initializeQuery();
    }

    @Override
    public void executeQuery() {
        source.query();
    }

    @Override
    public void onQueryComplete() {
        data.addAll(source.content());
        scroll = source.scroll();
        total = source.total();
        count = source.count();
    }


    @Override
    public void context(ApplicationContext context) {
        source.context(context);
    }

    @Override
    public void params(List<?> params) {
        this.source.params(params);
    }

    @Override
    public void postInit() {
        this.source.postInit();
    }


    @Override
    public <E extends ScrollSource<DATA_TYPE, ScrollSourceData>> void source(E source) {
        this.source = source;
    }

    @Override
    public void beforeDestroy() {
        this.source.beforeDestroy();
    }

}
