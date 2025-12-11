package com.thorekt.mdd.microservice.user_service.service.security.token;

import java.time.Instant;
import java.util.UUID;

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
public class JWTGeneratorService implements ITokenGeneratorService {

    private final JwtEncoder jwtEncoder;

    @Value("${security.jwt.issuer}")
    private String issuer;

    @Value("${security.jwt.expires-in}")
    private Long expiresIn;

    /**
     * Generate a JWT token for the given authentication
     * 
     * @param userUUID UUID of the user
     * @param username Username of the user
     * @return JWT token
     */
    public String generateToken(Authentication authentication, UUID userUUID) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .subject(userUUID.toString())
                .claim("username", authentication.getName())
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters
                .from(JwsHeader.with(SignatureAlgorithm.RS256).build(), claims);

        return jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }
}
