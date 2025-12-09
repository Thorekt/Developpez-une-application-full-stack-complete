package com.thorekt.mdd.microservice.article_service.dto.response;

import java.util.List;

import com.thorekt.mdd.microservice.article_service.dto.CommentDto;

public record CommentListResponse(List<CommentDto> comments) implements ApiResponse {

}
