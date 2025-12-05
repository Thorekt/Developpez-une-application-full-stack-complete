package com.thorekt.mdd.microservice.user_service.dto.response;

/**
 * Response DTO for errors
 * 
 * @author thorekt
 */
public record ErrorResponse(String error) implements ApiResponse {

}
