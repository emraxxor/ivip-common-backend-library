package com.github.emraxxor.ivip.common.scroll;

import com.github.emraxxor.ivip.common.es.DefaultScrollSourceData;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;


/**
 *
 * @author Attila Barna
 *
 * @param <DATA_TYPE>
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AbstractScrollResponse<DATA_TYPE, SCROLL_DATA extends DefaultScrollSourceData> implements ScrollSource<DATA_TYPE, SCROLL_DATA> {

    List<DATA_TYPE> content;

    SCROLL_DATA scroll;

    Long total;

    Integer count;

    List<?> params;

    public static Gson gson() {
        return new GsonBuilder().addDeserializationExclusionStrategy(DefaultActivityExclusionStrategy()).create();
    }

    public static ExclusionStrategy DefaultActivityExclusionStrategy() {
        return new ExclusionStrategy() {

            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getName().equalsIgnoreCase("join");
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        };
    }

    protected AbstractScrollResponse(SCROLL_DATA scroll) {
        this.scroll = scroll;
    }

    @Override
    public SCROLL_DATA scroll() {
        return scroll;
    }

    @Override
    public long total() {
        return this.total;
    }

    @Override
    public int size() {
        return this.scroll.size();
    }

    @Override
    public List<DATA_TYPE> content() {
        return this.content;
    }

    @Override
    public void params(List<?> params) {
        this.params = params;
    }

    public <T1,T2> void onProcess(T1 data, T2 element) {
        throw new IllegalStateException("Not implemented yet.");
    }

}
