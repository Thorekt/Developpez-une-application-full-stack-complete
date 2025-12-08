package com.thorekt.mdd.microservice.theme_service.config;

import java.security.interfaces.RSAPublicKey;

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
     * Security filter chain configuration for all endpoints.
     * 
     * @return SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> {
                }));
        return http.build();
    }
}
