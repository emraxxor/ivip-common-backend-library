package com.github.emraxxor.ivip.common.kafka;


import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * @author Attila Barna
 * @param <K>
 * @param <V>
 */
public interface KafkaConsumer<K,V> {
    void onMessage(ConsumerRecord<K,V> record);
}
