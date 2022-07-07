package com.github.emraxxor.ivip.common.es;

/**
 * Represents a data element
 * @param <T>
 */
public interface ESDataElement {

    String documentId();

    ESDataElement documentId(String documentId);

}
