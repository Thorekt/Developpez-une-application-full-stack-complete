package com.thorekt.mdd.microservice.article_service.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import com.thorekt.mdd.microservice.article_service.dto.CommentDto;
import com.thorekt.mdd.microservice.article_service.dto.request.CreateCommentRequest;
import com.thorekt.mdd.microservice.article_service.dto.response.ApiResponse;
import com.thorekt.mdd.microservice.article_service.dto.response.CommentListResponse;
import com.thorekt.mdd.microservice.article_service.exception.NotFoundException;
import com.thorekt.mdd.microservice.article_service.mapper.CommentMapper;
import com.thorekt.mdd.microservice.article_service.model.Article;
import com.thorekt.mdd.microservice.article_service.model.Comment;
import com.thorekt.mdd.microservice.article_service.service.CommentService;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {
        @Mock
        CommentService mockCommentService;

        @Mock
        CommentMapper mockCommentMapper;

        CommentController classUnderTest;

        @BeforeEach
        void setUp() {
                classUnderTest = new CommentController(mockCommentService, mockCommentMapper);
        }

        @Test
        public void findCommentsByArticleUuid_ShouldReturnListOfComments() {
                // Given
                UUID articleUuid = UUID.randomUUID();
                UUID commentUuid1 = UUID.randomUUID();
                UUID commentUuid2 = UUID.randomUUID();
                UUID userUuid1 = UUID.randomUUID();
                UUID userUuid2 = UUID.randomUUID();
                UUID themeUuid = UUID.randomUUID();
                UUID articleUserUuid = UUID.randomUUID();

                Article article = Article.builder().uuid(articleUuid).title("Article").content("content")
                                .userUuid(articleUserUuid).themeUuid(themeUuid).build();

                Comment comment1 = Comment.builder().uuid(commentUuid1).content("Comment 1")
                                .userUuid(userUuid1).article(article).build();
                Comment comment2 = Comment.builder().uuid(commentUuid2).content("Comment 2")
                                .userUuid(userUuid2).article(article).build();

                CommentDto commentDto1 = CommentDto.builder().uuid(commentUuid1).content("Comment 1")
                                .userUuid(userUuid1).article(article).createdAt(Instant.parse("2025-01-01T00:00:00Z"))
                                .build();
                CommentDto commentDto2 = CommentDto.builder().uuid(commentUuid2).content("Comment 2")
                                .userUuid(userUuid2).article(article).createdAt(Instant.parse("2025-01-02T00:00:00Z"))
                                .build();

                Mockito.when(mockCommentService.findCommentsByArticleUuid(articleUuid))
                                .thenReturn(List.of(comment1, comment2));
                Mockito.when(mockCommentMapper.toDto(comment1)).thenReturn(commentDto1);
                Mockito.when(mockCommentMapper.toDto(comment2)).thenReturn(commentDto2);

                // When
                ResponseEntity<ApiResponse> response = classUnderTest.findCommentsByArticleUuid(articleUuid);

                // Then
                assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
                CommentListResponse body = (CommentListResponse) response.getBody();
                assertEquals(2, body.comments().size());
                assertEquals("Comment 1", body.comments().get(0).getContent());
                assertEquals("Comment 2", body.comments().get(1).getContent());
                Mockito.verify(mockCommentService).findCommentsByArticleUuid(articleUuid);
                Mockito.verify(mockCommentMapper).toDto(comment1);
                Mockito.verify(mockCommentMapper).toDto(comment2);
        }

        @Test
        public void findCommentsByArticleUuid_ShouldHandleNotFoundException() {
                // Given
                UUID articleUuid = UUID.randomUUID();

                Mockito.when(mockCommentService.findCommentsByArticleUuid(articleUuid))
                                .thenThrow(new NotFoundException());

                // When
                ResponseEntity<ApiResponse> response = classUnderTest.findCommentsByArticleUuid(articleUuid);

                // Then
                assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
                Mockito.verify(mockCommentService).findCommentsByArticleUuid(articleUuid);
                Mockito.verifyNoInteractions(mockCommentMapper);
        }

        @Test
        public void findCommentsByArticleUuid_ShouldHandleIllegalArgumentException() {
                // Given
                UUID articleUuid = UUID.randomUUID();

                Mockito.when(mockCommentService.findCommentsByArticleUuid(articleUuid))
                                .thenThrow(new IllegalArgumentException("Invalid UUID"));

                // When
                ResponseEntity<ApiResponse> response = classUnderTest.findCommentsByArticleUuid(articleUuid);

                // Then
                assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
                Mockito.verify(mockCommentService).findCommentsByArticleUuid(articleUuid);
                Mockito.verifyNoInteractions(mockCommentMapper);
        }

        @Test
        public void findCommentsByArticleUuid_ShouldHandleGenericException() {
                // Given
                UUID articleUuid = UUID.randomUUID();

                Mockito.when(mockCommentService.findCommentsByArticleUuid(articleUuid))
                                .thenThrow(new RuntimeException("Database error"));

                // When
                ResponseEntity<ApiResponse> response = classUnderTest.findCommentsByArticleUuid(articleUuid);

                // Then
                assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
                Mockito.verify(mockCommentService).findCommentsByArticleUuid(articleUuid);
                Mockito.verifyNoInteractions(mockCommentMapper);
        }

        @Test
        public void createComment_ShouldCreateCommentSuccessfully() {
                // Given
                String userUuid = UUID.randomUUID().toString();
                String articleUuid = UUID.randomUUID().toString();
                String content = "New comment";
                CreateCommentRequest request = new CreateCommentRequest(articleUuid, content);

                Jwt mockJwt = Mockito.mock(Jwt.class);
                Mockito.when(mockJwt.getClaimAsString("sub")).thenReturn(userUuid);
                Mockito.doNothing().when(mockCommentService).createComment(articleUuid, userUuid, content);

                // When
                ResponseEntity<ApiResponse> response = classUnderTest.createComment(mockJwt, request);

                // Then
                assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
                Mockito.verify(mockCommentService).createComment(articleUuid, userUuid, content);
                Mockito.verifyNoInteractions(mockCommentMapper);
        }

        @Test
        public void createComment_ShouldHandleNotFoundException() {
                // Given
                String userUuid = UUID.randomUUID().toString();
                String articleUuid = UUID.randomUUID().toString();
                String content = "New comment";
                CreateCommentRequest request = new CreateCommentRequest(articleUuid, content);

                Jwt mockJwt = Mockito.mock(Jwt.class);
                Mockito.when(mockJwt.getClaimAsString("sub")).thenReturn(userUuid);
                Mockito.doThrow(new NotFoundException()).when(mockCommentService).createComment(articleUuid,
                                userUuid, content);

                // When
                ResponseEntity<ApiResponse> response = classUnderTest.createComment(mockJwt, request);

                // Then
                assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
                Mockito.verify(mockCommentService).createComment(articleUuid, userUuid, content);
                Mockito.verifyNoInteractions(mockCommentMapper);
        }

        @Test
        public void createComment_ShouldHandleIllegalArgumentException() {
                // Given
                String userUuid = UUID.randomUUID().toString();
                String articleUuid = UUID.randomUUID().toString();
                String content = "New comment";
                CreateCommentRequest request = new CreateCommentRequest(articleUuid, content);

                Jwt mockJwt = Mockito.mock(Jwt.class);
                Mockito.when(mockJwt.getClaimAsString("sub")).thenReturn(userUuid);
                Mockito.doThrow(new IllegalArgumentException("Invalid UUID")).when(mockCommentService).createComment(
                                articleUuid,
                                userUuid, content);

                // When
                ResponseEntity<ApiResponse> response = classUnderTest.createComment(mockJwt, request);

                // Then
                assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
                Mockito.verify(mockCommentService).createComment(articleUuid, userUuid, content);
                Mockito.verifyNoInteractions(mockCommentMapper);
        }

        @Test
        public void createComment_ShouldHandleGenericException() {
                // Given
                String userUuid = UUID.randomUUID().toString();
                String articleUuid = UUID.randomUUID().toString();
                String content = "New comment";
                CreateCommentRequest request = new CreateCommentRequest(articleUuid, content);

                Jwt mockJwt = Mockito.mock(Jwt.class);
                Mockito.when(mockJwt.getClaimAsString("sub")).thenReturn(userUuid);
                Mockito.doThrow(new RuntimeException("Database error")).when(mockCommentService).createComment(
                                articleUuid,
                                userUuid, content);

                // When
                ResponseEntity<ApiResponse> response = classUnderTest.createComment(mockJwt, request);

                // Then
                assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
                Mockito.verify(mockCommentService).createComment(articleUuid, userUuid, content);
                Mockito.verifyNoInteractions(mockCommentMapper);
        }

}
