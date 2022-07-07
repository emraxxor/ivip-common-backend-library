package com.github.emraxxor.ivip.common.kafka;

/**
 *
 * @param <K>
 * @param <V>
 * @author Attila Barna
 */
public interface ProducerEvent<K,V extends EventType> {
    K key();
    V value();
}
