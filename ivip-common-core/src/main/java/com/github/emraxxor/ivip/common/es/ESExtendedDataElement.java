package com.github.emraxxor.ivip.common.es;

/**
 * Specifies the required arguments of an element that will be inserted into the index.
 * @author attila
 *
 */
public interface ESExtendedDataElement<T extends AbstractBaseDataElement> {

    default String id() { return null; }

    String index();

    String type();

    default String routing() { return null; }

    ESOperationType operation();

    T data();

    default ESExtendedDataElement<T> operationIndex() { return null; }

    default ESExtendedDataElement<T> operationUpdate() { return null; }

    default ESExtendedDataElement<T> operationDelete() { return null; }
}
