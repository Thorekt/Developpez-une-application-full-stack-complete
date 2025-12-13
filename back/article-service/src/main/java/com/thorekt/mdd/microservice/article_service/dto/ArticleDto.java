package com.thorekt.mdd.microservice.article_service.dto;

import java.time.Instant;
import java.util.UUID;

import com.thorekt.mdd.microservice.article_service.dto.response.ApiResponse;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleDto implements ApiResponse {
    @NonNull
    private UUID uuid;

    @NonNull
    private String title;

    @NonNull
    private String content;

    @NonNull
    private UUID userUuid;

    @NonNull
    private UUID themeUuid;

    @NonNull
    private Instant createdAt;
}
