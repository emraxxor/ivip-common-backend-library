package com.github.emraxxor.ivip.common.es.manager;

import com.github.emraxxor.ivip.common.es.AbstractBaseDataElement;

import java.util.List;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * "DocumentManager" is a simple component for retrieving stored documents from elasticsearch.
 *
 * @see DefaultDocumentManager
 * @author Attila Barna
 */
public interface DocumentManager {

    <T extends AbstractBaseDataElement> Optional<T> findByDocumentId(String id, String index, Type type);

    <T extends AbstractBaseDataElement> Optional<T> findByField(List<Field> fields, IndexMetaData metaData, Type type);

    <T extends AbstractBaseDataElement> List<T> findByFieldList(List<Field> fields, IndexMetaData metaData, Type type, int size, int from);

    <T extends AbstractBaseDataElement> Optional<T> findByIdUsingMeta(String id, IndexMetaData meta, Type type);

    Long countByField(List<Field> fields, IndexMetaData metaData);

}
