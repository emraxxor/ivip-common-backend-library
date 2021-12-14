package com.github.emraxxor.ivip.common.feign.realms;

import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

public class IvipAuthRealmFeignConfig {

    @Configuration
    @ConfigurationProperties(prefix = "ivip.auth.feign.webauth")
    @Profile("!integration-test")
    static class OAuth2FeignClientProperties extends ClientCredentialsResourceDetails {

        public OAuth2FeignClientProperties() {
            this.setGrantType("client_credentials");
        }
    }

    @Bean
    @Profile("!integration-test")
    @ConditionalOnProperty(value = "ivip.auth.feign.webauth.enabled", havingValue = "true")
    public RequestInterceptor oauth2GWFeignClientRequestInterceptor(OAuth2FeignClientProperties details)
    {
        RealmConfigValidator.validate(details);
        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), details);
    }

}
