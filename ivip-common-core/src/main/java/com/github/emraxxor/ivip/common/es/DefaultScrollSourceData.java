package com.github.emraxxor.ivip.common.es;

import lombok.Getter;
import lombok.Setter;
import org.elasticsearch.search.sort.SortOrder;

/**
 *
 * Specifies the "pagination" for ES Container based elements
 *
 * @author Attila Barna
 */
@Getter
@Setter
public class DefaultScrollSourceData implements ScrollSourceData {

    protected String token;

    protected SortOrder sortOrder;

    protected Integer size = 50;

    protected String sortField;

    @Override
    public final String token() {
        return token;
    }

    @Override
    public SortOrder sortOrder() {
        return sortOrder;
    }

    @Override
    public Integer size() {
        return size;
    }

    @Override
    public String sortField() {
        return sortField;
    }
}
