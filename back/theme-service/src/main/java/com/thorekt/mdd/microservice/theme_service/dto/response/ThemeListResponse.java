package com.thorekt.mdd.microservice.theme_service.dto.response;

import java.util.List;

import com.thorekt.mdd.microservice.theme_service.dto.ThemeDto;

public record ThemeListResponse(List<ThemeDto> themes) implements ApiResponse {

}
