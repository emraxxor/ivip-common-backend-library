package com.github.emraxxor.ivip.common.kafka;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class DefaultEvent<K, V extends EventType> implements ProducerEvent<K,V> {

    K key;
    V value;

    @Override
    public K key() {
        return key;
    }

    @Override
    public V value() {
        return value;
    }
}
