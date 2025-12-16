package com.thorekt.mdd.microservice.user_service.service.security.token;

import java.util.UUID;

import org.springframework.security.core.Authentication;

/**
 * Service interface for generating security tokens
 * 
 * @author thorekt
 */
public interface ITokenGeneratorService {
    /**
     * Generate a security token for the given authentication and user UUID
     * 
     * @param authentication Authentication object
     * @param userUUID       UUID of the user
     * @return Generated security token as a String
     */
    public String generateToken(Authentication authentication, UUID userUUID);
}
