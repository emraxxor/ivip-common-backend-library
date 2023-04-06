package com.github.emraxxor.ivip.common.feign.realms;

import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

public interface IvipBaseFeignConfig<P extends AbstractClientProperties> {

    default String getAccessToken(
            P properties,
            KeycloakRestTemplate keycloakRestTemplate
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(
                String.format("grant_type=client_credentials&client_id=%s&client_secret=%s", properties.getClientId(), properties.getClientSecret()),
                headers);

        AccessTokenResponse response = keycloakRestTemplate.exchange(
                properties.getAccessTokenUri(),
                HttpMethod.POST,
                entity,
                AccessTokenResponse.class).getBody();

        return response.getToken();
    }

}
