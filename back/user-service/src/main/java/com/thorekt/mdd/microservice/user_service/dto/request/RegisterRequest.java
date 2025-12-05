package com.thorekt.mdd.microservice.user_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for user registration
 * 
 * @param email    User's email
 * @param username User's username
 * @param password User's password
 * 
 * @author Thorekt
 */
public record RegisterRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 3, max = 50) String username,
        @NotBlank @Size(min = 6, max = 50) String password) {
}
