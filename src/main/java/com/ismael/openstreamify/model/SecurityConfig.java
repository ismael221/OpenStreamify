package com.ismael.openstreamify.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecurityConfig {

    @Value("${app.security.public-key}")
    private String publicKeyEncoded;

    @Value("${app.security.private-key}")
    private String privateKeyEncoded;

    public String getPublicKeyEncoded() {
        return publicKeyEncoded;
    }

    public String getPrivateKeyEncoded() {
        return privateKeyEncoded;
    }
}
