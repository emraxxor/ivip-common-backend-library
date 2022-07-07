package com.github.emraxxor.ivip.common.scroll;

import com.github.emraxxor.ivip.common.es.ScrollSourceData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Getter
@Setter
public class DefaultScrollResponseData<DATA_TYPE, SCROLL extends ScrollSourceData> {

    final List<DATA_TYPE> data;

    SCROLL scroll;

    long total;

    int count;

    public DefaultScrollResponseData() {
        data = new ArrayList<>();
    }
}
