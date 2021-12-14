package com.github.emraxxor.ivip.common.configuration.service;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "ivip.minio.enabled", havingValue = "true")
@Slf4j
public class MinioServiceConfiguration {

    private final String server;
    private final String accessKey;
    private final String secretKey;

    public MinioServiceConfiguration(
            @Value("${ivip.minio.server}") String server,
            @Value("${ivip.minio.access-key}") String accessKey,
            @Value("${ivip.minio.secret-key}") String secretKey
    ) {
        this.server = server;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    @Bean
    MinioClient minioClientBean() {
        try {
            return new MinioClient(server, accessKey, secretKey);
        } catch (InvalidEndpointException | InvalidPortException e) {
            log.error(e.getMessage());
        }
        log.warn("Connection could not be established!");
        return null;
    }

}
