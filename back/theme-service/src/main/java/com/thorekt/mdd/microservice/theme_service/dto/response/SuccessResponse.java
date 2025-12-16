package com.thorekt.mdd.microservice.theme_service.dto.response;

/**
 * Response DTO for successful operations
 * 
 * @param message Success message
 * 
 * @author thorekt
 */
public record SuccessResponse(String message) implements ApiResponse {
}
