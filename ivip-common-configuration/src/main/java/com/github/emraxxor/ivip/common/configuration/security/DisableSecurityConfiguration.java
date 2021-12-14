package com.github.emraxxor.ivip.common.configuration.security;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
        value = "ivip.auth.enabled",
        havingValue = "false",
        matchIfMissing = true)
@EnableAutoConfiguration(excludeName = {
        "org.keycloak.adapters.springboot.KeycloakAutoConfiguration",
        "org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration",
        "org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration"})
public class DisableSecurityConfiguration {
}
