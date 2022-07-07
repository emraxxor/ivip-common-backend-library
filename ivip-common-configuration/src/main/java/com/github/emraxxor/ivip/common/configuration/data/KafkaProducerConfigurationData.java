package com.github.emraxxor.ivip.common.configuration.data;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConditionalOnProperty(value = "ivip.kafka.producer.enabled", havingValue = "true")
@ConfigurationProperties(prefix = "ivip.kafka.producer")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KafkaProducerConfigurationData {
    Boolean enabled;
    String topicName;
    Integer partition;
    Short replicas;
}
