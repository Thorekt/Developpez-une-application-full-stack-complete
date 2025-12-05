package com.thorekt.mdd.microservice.user_service.dto.response;

/**
 * Response DTO for authentication
 * 
 * @param token JWT token
 * @param error Error message, "n/a" if no error
 * 
 * @author thorekt
 */
public record AuthResponse(String token, String error) {

    /**
     * Constructor without error message, defaults to "n/a"
     * 
     * @param token JWT token
     */
    public AuthResponse(String token) {
        this(token, "n/a");
    }

}
