package com.thorekt.mdd.microservice.theme_service.config;

import java.security.interfaces.RSAPublicKey;

import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.converter.RsaKeyConverters;

import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Value("${security.jwt.public-key}")
    private Resource publicKeyResource;

    /**
     * JWT decoder bean using RSA public key.
     * 
     * @return JwtDecoder
     * @throws Exception
     */
    @Bean
    public JwtDecoder jwtDecoder() throws Exception {
        RSAPublicKey publicKey = RsaKeyConverters.x509().convert(publicKeyResource.getInputStream());
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    /**
     * Public endpoints: /actuator/**
     * 
     * @return SecurityFilterChain
     * @throws Exception
     */
    @Bean
    @Order(1)
    public SecurityFilterChain publicSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/actuator/**")
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }

    /**
     * Protected endpoints: everything else
     * 
     * @return SecurityFilterChain
     * @throws Exception
     */
    @Bean
    @Order(2)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> {
                }));

        return http.build();
    }
}
