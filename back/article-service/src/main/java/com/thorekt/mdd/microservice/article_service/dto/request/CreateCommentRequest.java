package com.thorekt.mdd.microservice.article_service.dto.request;

public record CreateCommentRequest(String articleUuid, String content) {

}
