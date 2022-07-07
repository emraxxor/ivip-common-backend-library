package com.github.emraxxor.ivip.common.filter;

import org.elasticsearch.index.query.BoolQueryBuilder;

/**
 * Interface for generating documents by high-level abstraction
 *
 * @author Attila Barna
 */
public interface DocumentBoolScrollQueryIF {

    void initQuery(BoolQueryBuilder qb);
}
