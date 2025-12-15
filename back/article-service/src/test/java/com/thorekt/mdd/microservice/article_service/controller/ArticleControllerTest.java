package com.thorekt.mdd.microservice.article_service.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

import com.thorekt.mdd.microservice.article_service.dto.ArticleDto;
import com.thorekt.mdd.microservice.article_service.dto.request.CreateArticleRequest;
import com.thorekt.mdd.microservice.article_service.dto.response.ApiResponse;
import com.thorekt.mdd.microservice.article_service.dto.response.ArticleListResponse;
import com.thorekt.mdd.microservice.article_service.exception.NotFoundException;
import com.thorekt.mdd.microservice.article_service.mapper.ArticleMapper;
import com.thorekt.mdd.microservice.article_service.model.Article;
import com.thorekt.mdd.microservice.article_service.service.ArticleService;

import utils.OrderEnum;

@ExtendWith(MockitoExtension.class)
public class ArticleControllerTest {
        @Mock
        ArticleService mockArticleService;

        @Mock
        ArticleMapper mockArticleMapper;

        ArticleController classUnderTest;

        @BeforeEach
        void setUp() {
                classUnderTest = new ArticleController(mockArticleService, mockArticleMapper);
        }

        @Test
        public void findArticlesByThemesUuidsInOrder_ShouldReturnListOfArticles() {
                // Given
                UUID themeUuid1 = UUID.randomUUID();
                UUID themeUuid2 = UUID.randomUUID();
                UUID articleUuid1 = UUID.randomUUID();
                UUID articleUuid2 = UUID.randomUUID();
                UUID userUuid1 = UUID.randomUUID();
                UUID userUuid2 = UUID.randomUUID();

                Article article1 = Article.builder().uuid(articleUuid1).title("Article 1").content("content 1")
                                .userUuid(userUuid1).themeUuid(themeUuid1).build();
                Article article2 = Article.builder().uuid(articleUuid2).title("Article 2").content("content 2")
                                .userUuid(userUuid2).themeUuid(themeUuid2).build();

                ArticleDto articleDto1 = ArticleDto.builder().uuid(articleUuid1).title("Article 1").content("content 1")
                                .userUuid(userUuid1).themeUuid(themeUuid1)
                                .createdAt(Instant.parse("2025-01-01T00:00:00Z")).build();
                ArticleDto articleDto2 = ArticleDto.builder().uuid(articleUuid2).title("Article 2").content("content 2")
                                .userUuid(userUuid2).themeUuid(themeUuid2)
                                .createdAt(Instant.parse("2025-01-02T00:00:00Z")).build();

                List<UUID> themeUuids = List.of(themeUuid1, themeUuid2);

                Mockito.when(mockArticleService.findAllArticlesByThemesUuidsInOrder(themeUuids, OrderEnum.DESC))
                                .thenReturn(List.of(article1, article2));
                Mockito.when(mockArticleMapper.toDto(article1)).thenReturn(articleDto1);
                Mockito.when(mockArticleMapper.toDto(article2)).thenReturn(articleDto2);

                // When
                ResponseEntity<ApiResponse> response = classUnderTest.findArticlesByThemesUuidsInOrder(themeUuids,
                                OrderEnum.DESC);

                // Then
                assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
                ArticleListResponse body = (ArticleListResponse) response.getBody();
                assertTrue(body.articles().size() == 2);
                assertEquals("Article 1", body.articles().get(0).getTitle());
                assertEquals("Article 2", body.articles().get(1).getTitle());
                Mockito.verify(mockArticleService).findAllArticlesByThemesUuidsInOrder(themeUuids, OrderEnum.DESC);
                Mockito.verify(mockArticleMapper).toDto(article1);
                Mockito.verify(mockArticleMapper).toDto(article2);
        }

        @Test
        public void findArticlesByThemesUuidsInOrder_ShouldHandleException() {
                // Given
                List<UUID> themeUuids = List.of(UUID.randomUUID(), UUID.randomUUID());

                Mockito.when(mockArticleService.findAllArticlesByThemesUuidsInOrder(themeUuids, OrderEnum.DESC))
                                .thenThrow(new RuntimeException("Database error"));

                // When
                ResponseEntity<ApiResponse> response = classUnderTest.findArticlesByThemesUuidsInOrder(themeUuids,
                                OrderEnum.DESC);

                // Then
                assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
                Mockito.verify(mockArticleService).findAllArticlesByThemesUuidsInOrder(themeUuids, OrderEnum.DESC);
                Mockito.verifyNoInteractions(mockArticleMapper);
        }

        @Test
        public void createArticle_ShouldCreateArticleAndReturnSuccessResponse() {
                // Given
                String userUuid = UUID.randomUUID().toString();
                String themeUuid = UUID.randomUUID().toString();
                String title = "New Article";
                String content = "Article content";
                CreateArticleRequest request = new CreateArticleRequest(themeUuid, title, content);

                Jwt mockJwt = Mockito.mock(Jwt.class);
                Mockito.when(mockJwt.getClaimAsString("sub")).thenReturn(userUuid);
                Mockito.doNothing().when(mockArticleService).createArticle(userUuid, themeUuid, title, content);

                // When
                ResponseEntity<ApiResponse> response = classUnderTest.createArticle(mockJwt, request);

                // Then
                assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
                Mockito.verify(mockArticleService).createArticle(userUuid, themeUuid, title, content);
                Mockito.verifyNoInteractions(mockArticleMapper);
        }

        @Test
        public void createArticle_ShouldHandleNotFoundException() {
                // Given
                String userUuid = UUID.randomUUID().toString();
                String themeUuid = UUID.randomUUID().toString();
                String title = "New Article";
                String content = "Article content";
                CreateArticleRequest request = new CreateArticleRequest(themeUuid, title, content);

                Jwt mockJwt = Mockito.mock(Jwt.class);
                Mockito.when(mockJwt.getClaimAsString("sub")).thenReturn(userUuid);
                Mockito.doThrow(new NotFoundException()).when(mockArticleService).createArticle(userUuid,
                                themeUuid, title, content);

                // When
                ResponseEntity<ApiResponse> response = classUnderTest.createArticle(mockJwt, request);

                // Then
                assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
                Mockito.verify(mockArticleService).createArticle(userUuid, themeUuid, title, content);
                Mockito.verifyNoInteractions(mockArticleMapper);
        }

        @Test
        public void createArticle_ShouldHandleIllegalArgumentException() {
                // Given
                String userUuid = UUID.randomUUID().toString();
                String themeUuid = UUID.randomUUID().toString();
                String title = "New Article";
                String content = "Article content";
                CreateArticleRequest request = new CreateArticleRequest(themeUuid, title, content);

                Jwt mockJwt = Mockito.mock(Jwt.class);
                Mockito.when(mockJwt.getClaimAsString("sub")).thenReturn(userUuid);
                Mockito.doThrow(new IllegalArgumentException("Invalid UUID")).when(mockArticleService).createArticle(
                                userUuid,
                                themeUuid, title, content);

                // When
                ResponseEntity<ApiResponse> response = classUnderTest.createArticle(mockJwt, request);

                // Then
                assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
                Mockito.verify(mockArticleService).createArticle(userUuid, themeUuid, title, content);
                Mockito.verifyNoInteractions(mockArticleMapper);
        }

        @Test
        public void createArticle_ShouldHandleGenericException() {
                // Given
                String userUuid = UUID.randomUUID().toString();
                String themeUuid = UUID.randomUUID().toString();
                String title = "New Article";
                String content = "Article content";
                CreateArticleRequest request = new CreateArticleRequest(themeUuid, title, content);

                Jwt mockJwt = Mockito.mock(Jwt.class);
                Mockito.when(mockJwt.getClaimAsString("sub")).thenReturn(userUuid);
                Mockito.doThrow(new RuntimeException("Database error")).when(mockArticleService).createArticle(userUuid,
                                themeUuid, title, content);

                // When
                ResponseEntity<ApiResponse> response = classUnderTest.createArticle(mockJwt, request);

                // Then
                assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
                Mockito.verify(mockArticleService).createArticle(userUuid, themeUuid, title, content);
                Mockito.verifyNoInteractions(mockArticleMapper);
        }

        @Test
        public void findArticleByUuid_ShouldReturnArticle() {
                // Given
                UUID articleUuid = UUID.randomUUID();
                UUID themeUuid = UUID.randomUUID();
                UUID userUuid = UUID.randomUUID();
                Article article = Article.builder().uuid(articleUuid).title("Article 1").content("content 1")
                                .userUuid(userUuid).themeUuid(themeUuid).build();
                ArticleDto articleDto = ArticleDto.builder().uuid(articleUuid).title("Article 1").content("content 1")
                                .userUuid(userUuid).themeUuid(themeUuid)
                                .createdAt(Instant.parse("2025-01-01T00:00:00Z")).build();

                Mockito.when(mockArticleService.findArticleByUuid(articleUuid)).thenReturn(article);
                Mockito.when(mockArticleMapper.toDto(article)).thenReturn(articleDto);

                // When
                ResponseEntity<ApiResponse> response = classUnderTest.findArticleByUuid(articleUuid);

                // Then
                assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
                ArticleDto body = (ArticleDto) response.getBody();
                assertEquals("Article 1", body.getTitle());
                Mockito.verify(mockArticleService).findArticleByUuid(articleUuid);
                Mockito.verify(mockArticleMapper).toDto(article);
        }

        @Test
        public void findArticleByUuid_ShouldHandleNotFoundException() {
                // Given
                UUID articleUuid = UUID.randomUUID();

                Mockito.when(mockArticleService.findArticleByUuid(articleUuid))
                                .thenThrow(new NotFoundException());

                // When
                ResponseEntity<ApiResponse> response = classUnderTest.findArticleByUuid(articleUuid);

                // Then
                assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
                Mockito.verify(mockArticleService).findArticleByUuid(articleUuid);
                Mockito.verifyNoInteractions(mockArticleMapper);
        }

        @Test
        public void findArticleByUuid_ShouldHandleIllegalArgumentException() {
                // Given
                UUID articleUuid = UUID.randomUUID();

                Mockito.when(mockArticleService.findArticleByUuid(articleUuid))
                                .thenThrow(new IllegalArgumentException("Invalid UUID"));

                // When
                ResponseEntity<ApiResponse> response = classUnderTest.findArticleByUuid(articleUuid);

                // Then
                assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
                Mockito.verify(mockArticleService).findArticleByUuid(articleUuid);
                Mockito.verifyNoInteractions(mockArticleMapper);
        }

        @Test
        public void findArticleByUuid_ShouldHandleGenericException() {
                // Given
                UUID articleUuid = UUID.randomUUID();

                Mockito.when(mockArticleService.findArticleByUuid(articleUuid))
                                .thenThrow(new RuntimeException("Database error"));

                // When
                ResponseEntity<ApiResponse> response = classUnderTest.findArticleByUuid(articleUuid);

                // Then
                assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
                Mockito.verify(mockArticleService).findArticleByUuid(articleUuid);
                Mockito.verifyNoInteractions(mockArticleMapper);
        }

}
