package com.github.emraxxor.ivip.common.es;

import com.github.emraxxor.ivip.common.response.Response;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * Minimum properties required to store the item.
 * Default data element
 *
 * @author Attila Barna
 */
@Getter
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AbstractBaseDataElement
            implements Response, ESDataElement,
            ESDefaultRoute, ESParentElement, ESDocument {

    /**
     * Unique identifier of the document
     */
    String documentId;

    /**
     * If "routing" is specified for a particular document, it will also be stored.
     */
    String routing;


    /**
     * If there is a parent for the document, the ID required for the parent is also stored.
     */
    String parentDocument;


    /**
     * Type of the document
     */
    String documentType;

    public final AbstractBaseDataElement routing(String r) {
        this.routing = r;
        return this;
    }

    public final String routing() {
        return this.routing;
    }

    public final String documentId() {
        return documentId;
    }

    public final String type() {
        return documentType;
    }

    @Override
    public AbstractBaseDataElement type(String type) {
        this.documentType = type;
        return this;
    }

    public final AbstractBaseDataElement documentId(String documentId) {
        this.documentId = documentId;
        return this;
    }

    public final String parentDocument() {
        return parentDocument;
    }

    public final AbstractBaseDataElement parentDocument(String parentDocument) {
        this.parentDocument = parentDocument;
        return this;
    }

}
