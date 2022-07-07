package com.github.emraxxor.ivip.common.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

/**
 *
 * @param <K>
 * @param <V>
 * @author Attila Barna
 */
public interface EventProducer<K,V,Z extends EventType> {
    ListenableFuture<SendResult<K,V>> sendEvent(ProducerEvent<K,Z> event) throws JsonProcessingException;
}
