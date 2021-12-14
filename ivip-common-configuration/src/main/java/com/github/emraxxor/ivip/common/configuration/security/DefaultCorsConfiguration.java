package com.github.emraxxor.ivip.common.configuration.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ConditionalOnProperty(
        value = "ivip.network.cors.enabled",
        havingValue = "true",
        matchIfMissing = true)
@Configuration
public class DefaultCorsConfiguration implements WebMvcConfigurer {


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("*")
                .exposedHeaders("Authorization")
                .allowCredentials(false);
    }

}
