package com.github.emraxxor.ivip.common.es.container;

import com.github.emraxxor.ivip.common.es.AbstractBaseDataElement;
import com.github.emraxxor.ivip.common.es.ESExtendedDataElement;
import com.github.emraxxor.ivip.common.es.type.ESSimpleResquestElement;
import org.elasticsearch.action.DocWriteResponse;

import java.util.List;

/**
 *
 * @author Attila Barna
 * @category infovip.core.elasticsearch.container
 *
 * @todo @missingdoc
 * @param <T>
 */
public interface ESContainerIF<T extends ESExtendedDataElement<?>> {

    void add(T document);

    void add(List<T> document);

    void flush();

    boolean isThereFreePlaceInTheQueue();

    int queueSize();

    int bulkSize();

    <R extends DocWriteResponse> R executeThenGet(T data);

    <R extends AbstractBaseDataElement> R create(T data);

    <R extends DocWriteResponse> R executeSynchronousRequest(T data);

    <R extends AbstractBaseDataElement> R createSynchronous(T data);

    <TDATAELEMENT extends AbstractBaseDataElement, TE extends ESExtendedDataElement<TDATAELEMENT>> void search(ESSimpleResquestElement<TDATAELEMENT, TE> element);
}
