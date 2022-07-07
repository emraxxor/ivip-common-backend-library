package com.github.emraxxor.ivip.common.configuration.data;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConditionalOnProperty(value = "ivip.elasticsearch.enabled", havingValue = "true")
@ConfigurationProperties(prefix = "ivip.elasticsearch")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ElasticsearchConfigurationData {
    String host;
    Integer port;
}
