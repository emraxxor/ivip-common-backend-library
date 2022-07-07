package com.github.emraxxor.ivip.common.feign.realms;

import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

public class RealmConfigValidator {

    public static void validate(ClientCredentialsResourceDetails rd) {
        if (nullOrMissing(rd.getAccessTokenUri())) {
            throw new IllegalArgumentException("Null or missing Access Token URI!");
        }

        if (nullOrMissing(rd.getClientId())) {
            throw new IllegalArgumentException("Null or missing ClientId!");
        }

        if (nullOrMissing(rd.getClientSecret())) {
            throw new IllegalArgumentException("Null or missing Client Secret!");
        }
    }

    private static boolean nullOrMissing(String str) {
        return str == null || str.isEmpty();
    }
}
