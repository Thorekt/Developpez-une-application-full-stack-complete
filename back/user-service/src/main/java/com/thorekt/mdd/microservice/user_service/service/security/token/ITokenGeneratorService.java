package com.thorekt.mdd.microservice.user_service.service.security.token;

import java.util.UUID;

import org.springframework.security.core.Authentication;

public interface ITokenGeneratorService {
    public String generateToken(Authentication authentication, UUID userUUID);
}
