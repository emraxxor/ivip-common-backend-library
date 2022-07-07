package com.github.emraxxor.ivip.common.es.type;

import com.github.emraxxor.ivip.common.es.AbstractBaseDataElement;
import com.github.emraxxor.ivip.common.es.ESDefaultRoute;
import com.github.emraxxor.ivip.common.es.ESExtendedDataElement;
import com.github.emraxxor.ivip.common.es.ESOperationType;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * Represents a data element that will be stored
 *
 * @param <T>
 * @author Attila Barna
 */
@Getter
@Setter
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DefaultDataElement<T extends AbstractBaseDataElement> implements ESExtendedDataElement<T>, ESDefaultRoute {

    String index;

    ESOperationType operation;

    T data;

    public static <T extends AbstractBaseDataElement> DefaultDataElement<T> create(T data, String index, String type, ESOperationType operation) {
        return new DefaultDataElement<>(index, data, type == null ? data.type() : type, operation, data.documentId(), data.routing());
    }

    protected DefaultDataElement(String index, T data, String type, ESOperationType operation, String documentId, String routing) {
        this.index = index;
        this.operation = operation;
        this.data = data;
        this.data.routing(routing);
        this.data.documentId(documentId);
        this.data.type(type);
    }

    @Override
    public String id() {
        return data.documentId();
    }

    @Override
    public String index() {
        return index;
    }

    @Override
    public String type() {
        return data.type();
    }

    @Override
    public String routing() {
        return this.data.routing();
    }

    @Override
    public ESOperationType operation() {
        return operation;
    }

    @Override
    public T data() {
        return data;
    }

    public DefaultDataElement<T> routing(String routing) {
        this.data().routing(routing);
        return this;
    }

    @Override
    public ESExtendedDataElement<T> operationIndex() {
        this.operation = ESOperationType.INDEX;
        return this;
    }

    @Override
    public ESExtendedDataElement<T> operationUpdate() {
        this.operation = ESOperationType.UPDATE;
        return this;
    }

    @Override
    public ESExtendedDataElement<T> operationDelete() {
        this.operation = ESOperationType.DELETE;
        return this;
    }
}
