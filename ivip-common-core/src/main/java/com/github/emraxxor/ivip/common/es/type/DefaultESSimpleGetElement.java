package com.github.emraxxor.ivip.common.es.type;

import com.github.emraxxor.ivip.common.es.AbstractBaseDataElement;
import com.google.common.collect.Lists;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Attila Barna
 *
 * @param <T>
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DefaultESSimpleGetElement<
        TDATAELEMENT extends AbstractBaseDataElement,
        T extends DefaultDataElement<TDATAELEMENT>>
                implements ESSimpleResquestElement<TDATAELEMENT, T> {

    final T elem;

    final String id;

    final String type;

    final String index;

    final Type dataType;

    final List<ExclusionStrategy> strategies;

    String routing;

    public static ExclusionStrategy defaultActivityExclusionStrategy() {
        return new ExclusionStrategy() {

            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                if ( f.getName().equalsIgnoreCase("join") )
                    return true;

                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        };
    }

    public static Gson gson() {
        return new GsonBuilder().addDeserializationExclusionStrategy(defaultActivityExclusionStrategy()).create();
    }

    public DefaultESSimpleGetElement(T elem, Type t) {
        this(elem,t, Lists.newArrayList());
    }

    public DefaultESSimpleGetElement(T elem, Type t, List<ExclusionStrategy> strategies) {
        this.elem = elem;
        this.index = elem.index();
        this.type = elem.type();
        this.id = elem.id();
        this.dataType = t;
        this.strategies = strategies;
    }

    @Override
    public String index() {
        return index;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public void response( TDATAELEMENT data) {
        this.elem.setData( data );
    }

    @Override
    public Type dataType() {
        return this.dataType;
    }

    @Override
    public TDATAELEMENT data() {
        return this.elem.data();
    }

    @Override
    public List<ExclusionStrategy> exclusionStrategies() {
        return strategies;
    }

    @Override
    public String routing() {
        return routing;
    }

    @Override
    public void routing(String r) {
        this.routing = r;
    }

    @Override
    public T elem() {
        return elem;
    }
}