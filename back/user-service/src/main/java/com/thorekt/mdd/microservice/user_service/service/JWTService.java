package com.thorekt.mdd.microservice.user_service.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.core.Authentication;

import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * Service for JWT management
 * 
 * @author thorekt
 */
@Service
@RequiredArgsConstructor
public class JWTService {

    private final JwtEncoder jwtEncoder;

    @Value("${security.jwt.issuer}")
    private String issuer;

    @Value("${security.jwt.expires-in}")
    private Long expiresIn;

    /**
     * Generate a JWT token for the given authentication
     * 
     * @param authentication
     * @return JWT token
     */
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .subject(authentication.getName())
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters
                .from(JwsHeader.with(SignatureAlgorithm.RS256).build(), claims);

        return jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }
}
