package com.github.emraxxor.ivip.common.scroll;

public interface ScrollContentEvent<DATA_ELEMENT, RESPONSE_ELEMENT> {
    void beforeProcess();

    void onProcess(DATA_ELEMENT element);

    void afterProcess();

    RESPONSE_ELEMENT getResponseElement();
}
