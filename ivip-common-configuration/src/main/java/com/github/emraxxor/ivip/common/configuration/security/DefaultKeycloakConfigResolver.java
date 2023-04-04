package com.github.emraxxor.ivip.common.configuration.security;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@ConditionalOnProperty(
        value = "ivip.auth.enabled",
        havingValue = "true"
)
@Configuration
@Profile(value = {"local-dev", "development", "production", "kube"})
public class DefaultKeycloakConfigResolver {

    @Bean
    public KeycloakSpringBootConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

}
