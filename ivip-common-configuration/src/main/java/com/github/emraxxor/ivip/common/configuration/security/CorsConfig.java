package com.github.emraxxor.ivip.common.configuration.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Attila Barna
 */
@Configuration
@ConditionalOnProperty(
        value = "ivip.network.cors.enabled",
        havingValue = "true",
        matchIfMissing = false)
public class CorsConfig implements WebMvcConfigurer {

    @Value("${ivip.network.cors.allowed-origins}")
    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedOriginPatterns(allowedOrigins)
                .allowedMethods("*")
                .exposedHeaders("Authorization")
                .allowCredentials(true);
    }

}
