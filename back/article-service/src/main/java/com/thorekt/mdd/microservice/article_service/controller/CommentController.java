package com.thorekt.mdd.microservice.article_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thorekt.mdd.microservice.article_service.dto.CommentDto;
import com.thorekt.mdd.microservice.article_service.dto.request.CreateCommentRequest;
import com.thorekt.mdd.microservice.article_service.dto.response.ApiResponse;
import com.thorekt.mdd.microservice.article_service.dto.response.CommentListResponse;
import com.thorekt.mdd.microservice.article_service.dto.response.ErrorResponse;
import com.thorekt.mdd.microservice.article_service.dto.response.SuccessResponse;
import com.thorekt.mdd.microservice.article_service.exception.NotFoundException;
import com.thorekt.mdd.microservice.article_service.mapper.CommentMapper;
import com.thorekt.mdd.microservice.article_service.model.Comment;
import com.thorekt.mdd.microservice.article_service.service.CommentService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Controller for managing comments.
 * 
 * @author Thorekt
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/article")
public class CommentController {
    /**
     * Comment service.
     */
    public final CommentService commentService;

    /**
     * Comment mapper.
     */
    public final CommentMapper commentMapper;

    /**
     * Find all comments by article UUID
     * 
     * @param articleUuid UUID of the article
     * @return ResponseEntity with list of comments
     */
    @GetMapping("/{uuid}/comments")
    public ResponseEntity<ApiResponse> findCommentsByArticleUuid(
            @PathVariable("uuid") @Validated UUID articleUuid) {
        try {
            List<Comment> comments = commentService.findCommentsByArticleUuid(articleUuid);
            List<CommentDto> commentDtos = comments.stream()
                    .map(commentMapper::toDto)
                    .toList();
            CommentListResponse response = new CommentListResponse(commentDtos);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(new ErrorResponse("INVALID_FORMAT"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("INTERNAL_SERVER_ERROR"));
        }
    }

    /**
     * Create a new comment for an article
     * 
     * @param jwt                  authenticated user
     * @param createCommentRequest request body
     * @return ResponseEntity with status
     */
    @PostMapping("/comment")
    public ResponseEntity<ApiResponse> createComment(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody CreateCommentRequest createCommentRequest) {
        String userUuid = jwt.getClaimAsString("sub");
        try {
            commentService.createComment(
                    createCommentRequest.articleUuid(),
                    userUuid,
                    createCommentRequest.content());
            return ResponseEntity.ok().body(new SuccessResponse("COMMENT_CREATED"));
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(new ErrorResponse("INVALID_FORMAT"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("INTERNAL_SERVER_ERROR"));
        }
    }

}
