package com.thorekt.mdd.microservice.article_service.dto.request;

/**
 * Request DTO for creating a new article.
 * 
 * @param themeUuid UUID of the theme
 * @param title     Title of the article
 * @param content   Content of the article
 */
public record CreateArticleRequest(String themeUuid, String title, String content) {

}
