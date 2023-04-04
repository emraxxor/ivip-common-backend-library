package com.github.emraxxor.ivip.common.kafka;


import com.github.emraxxor.ivip.common.configuration.data.KafkaProducerConfigurationData;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 *
 * @param <K>
 * @param <V>
 * @author Attila Barna
 */
@FieldDefaults(level = AccessLevel.PROTECTED)
@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventProducer<K,V,Z extends EventType> implements EventProducer<K,V,Z> {

    final KafkaProducerConfigurationData kafkaProducerConfigurationData;

    final KafkaTemplate<K,V> kafkaTemplate;

    protected abstract V transform(ProducerEvent<K,Z> event);

    public ListenableFuture<SendResult<K,V>> sendEvent(ProducerEvent<K,Z> event)  {
        K key = event.key();
        V value = transform(event);
        ProducerRecord<K,V> producerRecord = new ProducerRecord<>(kafkaProducerConfigurationData.getTopicName(), key, value);
        ListenableFuture<SendResult<K,V>> listenableFuture =  kafkaTemplate.send(producerRecord);

        listenableFuture.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error(ex.getMessage(), ex);
            }

            @Override
            public void onSuccess(SendResult<K, V> result) {
                log.debug(result.toString());
            }
        });
        return listenableFuture;
    }

}
