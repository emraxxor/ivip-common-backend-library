package com.github.emraxxor.ivip.common.feign.realms;

import lombok.Data;

@Data
public class AbstractClientProperties {
    private String clientId;
    private String clientSecret;
    private String accessTokenUri;
}
