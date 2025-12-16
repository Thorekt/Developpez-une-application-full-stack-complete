package com.thorekt.mdd.microservice.user_service.dto.request;

/**
 * Request DTO for updating user information
 * 
 * @param email    New email of the user
 * @param username New username of the user
 * @param password New password of the user
 * 
 * @author thorekt
 */
public record UpdateRequest(
                String email,
                String username,
                String password) {

}
