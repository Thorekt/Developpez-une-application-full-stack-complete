package com.thorekt.mdd.microservice.theme_service.dto.response;

import java.util.List;

import com.thorekt.mdd.microservice.theme_service.dto.ThemeDto;

/**
 * Response DTO for a list of themes
 * 
 * @param themes List of ThemeDto
 * 
 * @author thorekt
 */
public record ThemeListResponse(List<ThemeDto> themes) implements ApiResponse {

}
