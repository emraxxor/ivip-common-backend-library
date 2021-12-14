package com.github.emraxxor.ivip.common.configuration.swagger;

import com.fasterxml.classmate.TypeResolver;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.AuthorizationCodeGrantBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Currency;
import java.util.List;


/**
 *
 * @author Attila Barna
 */
@Configuration
@ConditionalOnProperty(
        value = "ivip.swagger.enabled",
        havingValue = "true"
)
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {

    @Value("${keycloak.auth-server-url:}")
    private String authServerUrl;

    @Value("${keycloak.realm:}")
    private String realm;

    @Value("${keycloak.resource:}")
    private String clientId;

    @Getter
    private static class SwaggerPageable {

        @ApiParam(value = "Number of records per page", example = "0")
        private Integer size;

        @ApiParam(value = "Results page you want to retrieve (0..N)", example = "0")
        private Integer page;

        @ApiParam(value = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.")
        private String sort;

    }

    @Bean
    @ConditionalOnProperty(
            value = "ivip.auth.enabled",
            havingValue = "false"
    )
    public Docket apiWithoutSecurity(TypeResolver typeResolver) {
        return new Docket(DocumentationType.SWAGGER_2)
                .alternateTypeRules(createAlternateTypeRules(typeResolver))
                .directModelSubstitute(Pageable.class, SwaggerPageable.class)
                .directModelSubstitute(OffsetDateTime.class, String.class)
                .directModelSubstitute(Currency.class, String.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.github.emraxxor"))
                .build().apiInfo(apiInfo());
    }


    @Bean
    @ConditionalOnProperty(
            value = "ivip.auth.enabled",
            havingValue = "true"
    )
    public Docket api(TypeResolver typeResolver) {
        return new Docket(DocumentationType.SWAGGER_2)
                .ignoredParameterTypes(AuthenticationPrincipal.class)
                .alternateTypeRules(createAlternateTypeRules(typeResolver))
                .directModelSubstitute(Pageable.class, SwaggerPageable.class)
                .directModelSubstitute(OffsetDateTime.class, String.class)
                .directModelSubstitute(Currency.class, String.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.github.emraxxor"))
                .paths(PathSelectors.any())
                .build()
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(List.of(securityScheme(), apiKey()))
                .apiInfo(apiInfo());
    }

    private AlternateTypeRule[] createAlternateTypeRules(TypeResolver typeResolver) {
        return new AlternateTypeRule[]{
                AlternateTypeRules.newRule(typeResolver.resolve(DeferredResult.class, typeResolver.resolve(FileSystemResource.class)), typeResolver.resolve(MultipartFile.class)),
                AlternateTypeRules.newRule(Serializable.class, Object.class),
                AlternateTypeRules.newRule(typeResolver.resolve(List.class, Serializable.class), typeResolver.resolve(List.class, Object.class)),
                AlternateTypeRules.newRule(Comparable.class, Object.class),
                AlternateTypeRules.newRule(typeResolver.resolve(List.class, Comparable.class), typeResolver.resolve(List.class, Object.class)),
        };
    }

    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder().clientId(clientId).realm(realm).build();
    }

    private SecurityScheme securityScheme() {
        GrantType grantType = new AuthorizationCodeGrantBuilder()
                .tokenEndpoint((b) -> b.url(authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token").tokenName("token").build())
                .tokenRequestEndpoint(b -> b.url(authServerUrl + "/realms/" + realm + "/protocol/openid-connect/auth").clientIdName(clientId).build())
                .build();

        return new OAuthBuilder().name("KeyCloak OAuth").grantTypes(Collections.singletonList(grantType)).build();
    }


    private SecurityContext securityContext() {
        var builder = SecurityContext.builder().securityReferences(List.of(new SecurityReference("KeyCloak OAuth", new AuthorizationScope[]{}), new SecurityReference("Authorization", new AuthorizationScope[]{})));
        return builder.build();
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                " API",
                "IVIP DEFAULT API.",
                "1.0",
                "Terms of service",
                new Contact("Attila Barna", "https://github.com/emraxxor", "attila.barna.wr@gmail.com"),
                "License of API",
                "#TODO_URL",
                Collections.emptyList()
        );
    }
}
