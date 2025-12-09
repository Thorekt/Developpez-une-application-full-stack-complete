package com.thorekt.mdd.microservice.article_service.dto.request;

public record CreateArticleRequest(String themeUuid, String title, String content) {

}
