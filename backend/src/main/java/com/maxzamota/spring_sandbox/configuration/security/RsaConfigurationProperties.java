package com.maxzamota.spring_sandbox.configuration.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "rsa")
public record RsaConfigurationProperties(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
}
