package com.github.emraxxor.ivip.common.feign.realms;

import feign.RequestInterceptor;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

public class IvipAuthRealmFeignConfig implements IvipBaseFeignConfig<IvipAuthRealmFeignConfig.OAuth2FeignClientProperties>  {


    @Configuration
    @ConfigurationProperties(prefix = "ivip.auth.feign.webauth")
    @Profile("!integration-test")
    static class OAuth2FeignClientProperties extends AbstractClientProperties {}

    @Bean
    @Profile("!integration-test")
    @ConditionalOnProperty(value = "ivip.auth.feign.webauth.enabled", havingValue = "true")
    public RequestInterceptor requestInterceptor(
            IvipAuthRealmFeignConfig.OAuth2FeignClientProperties properties,
            KeycloakRestTemplate keycloakRestTemplate
    ) {
        return template -> template.header("Authorization", String.format("Bearer %s", getAccessToken(properties, keycloakRestTemplate)));
    }

}
