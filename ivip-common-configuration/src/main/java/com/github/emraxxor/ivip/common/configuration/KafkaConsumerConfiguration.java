package com.github.emraxxor.ivip.common.configuration;

import com.github.emraxxor.ivip.common.configuration.data.KafkaConsumerConfigurationData;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@ConditionalOnProperty(value = "ivip.kafka.consumer.enabled", havingValue = "true")
@EnableKafka
@RequiredArgsConstructor
public class KafkaConsumerConfiguration {
    private final KafkaConsumerConfigurationData configuration;
}
