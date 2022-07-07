package com.github.emraxxor.ivip.common.configuration;

import com.github.emraxxor.ivip.common.configuration.data.ElasticsearchConfigurationData;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

@ConditionalOnProperty(value = "ivip.elasticsearch.enabled", havingValue = "true")
@Configuration
@RequiredArgsConstructor
public class ElasticsearchConfiguration  extends AbstractElasticsearchConfiguration {

    private final ElasticsearchConfigurationData config;

    @Bean(name = "elasticsearchClient")
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration
                                                            .builder()
                                                            .connectedTo(String.format("%s:%s", config.getHost(), config.getPort() ))
                                                            .build();
        return RestClients.create(clientConfiguration).rest();
    }
}
