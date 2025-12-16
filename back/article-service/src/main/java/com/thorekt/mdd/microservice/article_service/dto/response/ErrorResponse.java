package com.thorekt.mdd.microservice.article_service.dto.response;

/**
 * Response DTO for errors
 * 
 * @param error Error message
 * @author thorekt
 */
public record ErrorResponse(String error) implements ApiResponse {

}
