package com.thorekt.mdd.microservice.user_service.dto.response;

/**
 * Response DTO for authentication
 * 
 * @param token JWT token
 * 
 * @author thorekt
 */
public record AuthResponse(String token) implements ApiResponse {
}
