package com.thorekt.mdd.microservice.user_service.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for login
 * 
 * @param loginValue User login value (email or username)
 * @param password   User password
 * 
 * @author thorekt
 */
public record LoginRequest(
        @NotBlank String login,
        @NotBlank String password) {

}
