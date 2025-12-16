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

/**
 * Security configuration for the Theme Service
 * 
 * @author thorekt
 */
@Configuration
@org.springframework.context.annotation.Profile("!test")
public class SecurityConfig {

    /**
     * Public key resource for JWT verification.
     */
    @Value("${security.jwt.public-key}")
    private Resource publicKeyResource;

    /**
     * JWT decoder bean using RSA public key.
     * 
     * @return JwtDecoder
     * @throws Exception When key conversion fails
     */
    @Bean
    public JwtDecoder jwtDecoder() throws Exception {
        RSAPublicKey publicKey = RsaKeyConverters.x509().convert(publicKeyResource.getInputStream());
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    /**
     * Public endpoints: /actuator/**
     * 
     * @param http HttpSecurity instance
     * @return SecurityFilterChain
     * @throws Exception if security configuration fails
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
     * @param http HttpSecurity instance
     * @return SecurityFilterChain
     * @throws Exception if security configuration fails
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
