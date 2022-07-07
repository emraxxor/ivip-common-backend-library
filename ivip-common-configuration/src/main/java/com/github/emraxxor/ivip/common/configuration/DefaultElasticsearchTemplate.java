package com.github.emraxxor.ivip.common.configuration;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(value = "ivip.elasticsearch.enabled", havingValue = "true")
@Primary
@Component
public class DefaultElasticsearchTemplate extends ElasticsearchRestTemplate {

    public DefaultElasticsearchTemplate(RestHighLevelClient client) {
        super(client);
    }
}
