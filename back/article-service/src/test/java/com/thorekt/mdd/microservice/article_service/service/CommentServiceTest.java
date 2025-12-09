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
import com.thorekt.mdd.microservice.article_service.model.Comment;
import com.thorekt.mdd.microservice.article_service.repository.ArticleRepository;
import com.thorekt.mdd.microservice.article_service.repository.CommentRepository;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @Mock
    CommentRepository mockCommentRepository;

    @Mock
    ArticleRepository mockArticleRepository;

    CommentService classUnderTest;

    @BeforeEach
    void setUp() {
        classUnderTest = new CommentService(mockCommentRepository, mockArticleRepository);
    }

    @Test
    public void findCommentsByArticleUuid_ShouldInvokeRepositoryMethod() {
        // Given
        Article article = Article.builder()
                .uuid(UUID.randomUUID())
                .title("Test Article")
                .content("This is a test article")
                .userUuid(UUID.randomUUID())
                .themeUuid(UUID.randomUUID())
                .build();

        List<Comment> comments = List.of(
                Comment.builder()
                        .uuid(UUID.randomUUID())
                        .article(article)
                        .userUuid(UUID.randomUUID())
                        .content("This is a test comment")
                        .createdAt("2024-01-01T00:00:00Z")
                        .build(),
                Comment.builder()
                        .uuid(UUID.randomUUID())
                        .article(article)
                        .userUuid(UUID.randomUUID())
                        .content("This is another test comment")
                        .createdAt("2024-01-02T00:00:00Z")
                        .build());

        Mockito.when(mockArticleRepository.findByUuid(article.getUuid()))
                .thenReturn(article);

        Mockito.when(mockCommentRepository.findAllByArticleUuidInOrderByCreatedAtDesc(article.getUuid()))
                .thenReturn(comments);

        // When
        List<Comment> result = classUnderTest.findCommentsByArticleUuid(article.getUuid());

        // Then
        Mockito.verify(mockArticleRepository).findByUuid(article.getUuid());
        Mockito.verify(mockCommentRepository).findAllByArticleUuidInOrderByCreatedAtDesc(article.getUuid());
        assertEquals(comments, result);
    }

    @Test
    public void findCommentsByArticleUuid_ShouldThrowNotFoundException_whenArticleDoesNotExist() {
        // Given
        UUID articleUuid = UUID.randomUUID();

        Mockito.when(mockArticleRepository.findByUuid(articleUuid))
                .thenReturn(null);

        // When / Then
        try {
            classUnderTest.findCommentsByArticleUuid(articleUuid);
            assertEquals(true, false); // Force fail if no exception is thrown
        } catch (Exception e) {
            assertEquals(com.thorekt.mdd.microservice.article_service.exception.NotFoundException.class, e.getClass());
        }

        // Then
        Mockito.verify(mockArticleRepository).findByUuid(articleUuid);
        Mockito.verifyNoMoreInteractions(mockCommentRepository);
    }

    @Test
    public void createComment_ShouldInvokeRepositoryMethod() {
        // Given
        UUID articleUuid = UUID.randomUUID();
        UUID userUuid = UUID.randomUUID();
        String content = "This is a test comment";
        Article article = Article.builder()
                .uuid(articleUuid)
                .title("Test Article")
                .content("This is a test article")
                .userUuid(UUID.randomUUID())
                .themeUuid(UUID.randomUUID())
                .build();

        Mockito.when(mockArticleRepository.findByUuid(articleUuid))
                .thenReturn(article);

        // When
        classUnderTest.createComment(articleUuid.toString(), userUuid.toString(), content);

        // Then
        Mockito.verify(mockArticleRepository).findByUuid(articleUuid);
        Mockito.verify(mockCommentRepository).save(Mockito.argThat(comment -> comment.getArticle().equals(article) &&
                comment.getUserUuid().equals(userUuid) &&
                comment.getContent().equals(content)));
    }

    @Test
    public void createComment_ShouldThrowNotFoundException_whenArticleDoesNotExist() {
        // Given
        UUID articleUuid = UUID.randomUUID();
        UUID userUuid = UUID.randomUUID();
        String content = "This is a test comment";
        Mockito.when(mockArticleRepository.findByUuid(articleUuid))
                .thenReturn(null);

        // When / Then
        try {
            classUnderTest.createComment(articleUuid.toString(), userUuid.toString(), content);
            assertEquals(true, false); // Force fail if no exception is thrown
        } catch (Exception e) {
            assertEquals(NotFoundException.class, e.getClass());
        }

        // Then
        Mockito.verify(mockArticleRepository).findByUuid(articleUuid);
        Mockito.verifyNoMoreInteractions(mockCommentRepository);
    }
}
