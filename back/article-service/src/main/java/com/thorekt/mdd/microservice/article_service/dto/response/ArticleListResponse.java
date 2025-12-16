package com.thorekt.mdd.microservice.article_service.dto.response;

import java.util.List;

import com.thorekt.mdd.microservice.article_service.dto.ArticleDto;

/**
 * Response DTO for a list of articles.
 * 
 * @param articles List of ArticleDto
 */
public record ArticleListResponse(List<ArticleDto> articles) implements ApiResponse {

}
