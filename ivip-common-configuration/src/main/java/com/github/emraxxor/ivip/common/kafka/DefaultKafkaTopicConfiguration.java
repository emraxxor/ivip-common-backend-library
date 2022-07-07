package com.github.emraxxor.ivip.common.kafka;

import com.github.emraxxor.ivip.common.configuration.data.KafkaProducerConfigurationData;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "ivip.kafka.producer.enabled", havingValue = "true")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class DefaultKafkaTopicConfiguration {

    private final KafkaProducerConfigurationData config;

    @Bean
    public NewTopic defaultTopic() {
        return new NewTopic(config.getTopicName(), config.getPartition(), config.getReplicas() );
    }

}
