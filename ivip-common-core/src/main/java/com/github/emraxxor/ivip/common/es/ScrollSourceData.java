package com.github.emraxxor.ivip.common.es;

import org.elasticsearch.search.sort.SortOrder;

public interface ScrollSourceData {

    String token();

    SortOrder sortOrder();

    Integer size();

    String sortField();
}
