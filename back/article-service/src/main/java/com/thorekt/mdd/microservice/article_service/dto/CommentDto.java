package com.thorekt.mdd.microservice.article_service.dto;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thorekt.mdd.microservice.article_service.dto.response.ApiResponse;
import com.thorekt.mdd.microservice.article_service.model.Article;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto implements ApiResponse {
    @NonNull
    private UUID uuid;

    @NonNull
    private String content;

    @NonNull
    @JsonIgnore
    private Article article;

    @NonNull
    private UUID userUuid;

    @NonNull
    private Instant createdAt;

}
