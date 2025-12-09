package com.thorekt.mdd.microservice.theme_service.dto;

import java.util.UUID;

import com.thorekt.mdd.microservice.theme_service.dto.response.ApiResponse;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThemeDto implements ApiResponse {
    private UUID uuid;

    @NonNull
    private String title;

    @NonNull
    private String description;
}