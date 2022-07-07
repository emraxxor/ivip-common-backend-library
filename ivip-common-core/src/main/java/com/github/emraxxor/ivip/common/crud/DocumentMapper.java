package com.github.emraxxor.ivip.common.crud;

import com.github.emraxxor.ivip.common.es.AbstractBaseDataElement;

/**
 * Simple mapper for documents
 * @param <E>
 * @param <I>
 */
public interface DocumentMapper<E extends AbstractBaseDataElement,I extends AbstractBaseDataElement> {

    E mapToUpdate(I from, I to);

    default E mapToCreate(I from) { return (E) from; }

    default E mapToDelete(I from) { return (E) from; }
}
