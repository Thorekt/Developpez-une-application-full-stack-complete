package com.thorekt.mdd.microservice.article_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.thorekt.mdd.microservice.article_service.exception.NotFoundException;
import com.thorekt.mdd.microservice.article_service.model.Article;
import com.thorekt.mdd.microservice.article_service.repository.ArticleRepository;

import utils.OrderEnum;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {
    @Mock
    ArticleRepository mockArticleRepository;

    ArticleService classUnderTest;

    @BeforeEach
    void setUp() {
        classUnderTest = new ArticleService(mockArticleRepository);
    }

    @Test
    public void findAllArticlesByThemesUuidsInOrder_shouldReturnArticlesInDescendingOrder() {
        // Given
        OrderEnum order = OrderEnum.DESC;
        List<UUID> themeUuids = List.of(UUID.randomUUID());
        List<Article> expectedArticles = List.of(
                Article.builder()
                        .uuid(UUID.randomUUID())
                        .title("Article 1")
                        .content("Content 1")
                        .userUuid(UUID.randomUUID())
                        .themeUuid(UUID.randomUUID())
                        .build(),
                Article.builder()
                        .uuid(UUID.randomUUID())
                        .title("Article 2")
                        .content("Content 2")
                        .userUuid(UUID.randomUUID())
                        .themeUuid(UUID.randomUUID())
                        .build());
        Mockito.when(mockArticleRepository.findByThemeUuidInOrderByCreatedAtDesc(themeUuids))
                .thenReturn(expectedArticles);

        // When
        List<Article> result = classUnderTest.findAllArticlesByThemesUuidsInOrder(themeUuids, order);

        // Then
        Mockito.verify(mockArticleRepository).findByThemeUuidInOrderByCreatedAtDesc(themeUuids);
        assertEquals(expectedArticles, result);
        Mockito.verify(mockArticleRepository, Mockito.never()).findByThemeUuidInOrderByCreatedAtAsc(themeUuids);
    }

    @Test
    public void findAllArticlesByThemesUuidsInOrder_shouldReturnArticlesInAscendingOrder() {
        // Given
        OrderEnum order = OrderEnum.ASC;
        List<UUID> themeUuids = List.of(UUID.randomUUID());
        List<Article> expectedArticles = List.of(
                Article.builder()
                        .uuid(UUID.randomUUID())
                        .title("Article 1")
                        .content("Content 1")
                        .userUuid(UUID.randomUUID())
                        .themeUuid(UUID.randomUUID())
                        .build(),
                Article.builder()
                        .uuid(UUID.randomUUID())
                        .title("Article 2")
                        .content("Content 2")
                        .userUuid(UUID.randomUUID())
                        .themeUuid(UUID.randomUUID())
                        .build());

        Mockito.when(mockArticleRepository.findByThemeUuidInOrderByCreatedAtAsc(themeUuids))
                .thenReturn(expectedArticles);

        // When
        List<Article> result = classUnderTest.findAllArticlesByThemesUuidsInOrder(themeUuids, order);

        // Then
        Mockito.verify(mockArticleRepository).findByThemeUuidInOrderByCreatedAtAsc(themeUuids);
        assertEquals(expectedArticles, result);
        Mockito.verify(mockArticleRepository, Mockito.never()).findByThemeUuidInOrderByCreatedAtDesc(themeUuids);
    }

    @Test
    public void createArticle_ShouldInvokeRepositorySaveMethod() {
        // Given
        String userUuid = UUID.randomUUID().toString();
        String themeUuid = UUID.randomUUID().toString();
        String title = "Test Article";
        String content = "This is a test article";

        // When
        classUnderTest.createArticle(userUuid, themeUuid, title, content);

        // Then
        Mockito.verify(mockArticleRepository)
                .save(Mockito.argThat(article -> article.getUserUuid().toString().equals(userUuid)
                        && article.getThemeUuid().toString().equals(themeUuid)
                        && article.getTitle().equals(title)
                        && article.getContent().equals(content)));

    }

    @Test
    public void findArticleByUuid_ShouldReturnArticle_whenArticleExists() {
        // Given
        UUID articleUuid = UUID.randomUUID();
        Article expectedArticle = Article.builder()
                .uuid(articleUuid)
                .title("Test Article")
                .content("This is a test article")
                .userUuid(UUID.randomUUID())
                .themeUuid(UUID.randomUUID())
                .build();

        Mockito.when(mockArticleRepository.findByUuid(articleUuid))
                .thenReturn(expectedArticle);

        // When
        Article result = classUnderTest.findArticleByUuid(articleUuid);

        // Then
        Mockito.verify(mockArticleRepository).findByUuid(articleUuid);
        assertEquals(expectedArticle, result);
    }

    @Test
    public void findArticleByUuid_ShouldThrowNotFoundException_whenArticleDoesNotExist() {
        // Given
        UUID articleUuid = UUID.randomUUID();
        Mockito.when(mockArticleRepository.findByUuid(articleUuid))
                .thenReturn(null);

        // When / Then
        try {
            classUnderTest.findArticleByUuid(articleUuid);
            assertEquals(true, false); // Force fail if no exception is thrown
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass());
        }

        // Then
        Mockito.verify(mockArticleRepository).findByUuid(articleUuid);
    }

}
