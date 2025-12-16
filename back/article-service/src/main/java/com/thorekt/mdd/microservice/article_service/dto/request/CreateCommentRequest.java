package com.thorekt.mdd.microservice.article_service.dto.request;

/**
 * Request DTO for creating a new comment.
 * 
 * @param articleUuid UUID of the article
 * @param content     Content of the comment
 */
public record CreateCommentRequest(String articleUuid, String content) {

}
