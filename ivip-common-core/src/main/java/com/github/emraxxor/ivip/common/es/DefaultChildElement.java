package com.github.emraxxor.ivip.common.es;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class DefaultChildElement<T extends AbstractBaseDataElement> implements ESChildElement<T>  {

    String index;

    ESOperationType operation;

    T data;

    static <T extends AbstractBaseDataElement> DefaultChildElement<T> create(String parent, T data, ESOperationType operation, String index, String type) {
        return new DefaultChildElement<>(parent, data, operation, index, type);
    }

    protected DefaultChildElement(String parent, T data, ESOperationType operation, String index, String type) {
        this.operation = operation;
        this.data = data;
        this.index = index;
        this.data.parentDocument(parent);
        this.data.type(type);
    }

    @Override
    public String id() {
        return data.documentId();
    }

    @Override
    public String parent() {
        return this.data().parentDocument();
    }

    public DefaultChildElement<T> setParent(String parent) {
        this.data().parentDocument(parent);
        return this;
    }

    @Override
    public String index() {
        return index;
    }

    @Override
    public String type() {
        return data().type();
    }

    @Override
    public ESOperationType operation() {
        return operation;
    }

    @Override
    public T data() {
        return data;
    }

    public DefaultChildElement<T> setOperation(ESOperationType operation) {
        this.operation = operation;
        return this;
    }

    public DefaultChildElement<T> setIndex(String index) {
        this.index = index;
        return this;
    }

    public DefaultChildElement<T> setType(String type) {
        this.data().type(type);
        return this;
    }

    public void setData(T data) {
        this.data = data;
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
