package com.thorekt.mdd.microservice.article_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.thorekt.mdd.microservice.article_service.exception.NotFoundException;
import com.thorekt.mdd.microservice.article_service.model.Article;
import com.thorekt.mdd.microservice.article_service.model.Comment;
import com.thorekt.mdd.microservice.article_service.repository.ArticleRepository;
import com.thorekt.mdd.microservice.article_service.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service for managing comments related to articles.
 * 
 * @author Thorekt
 */
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    private final ArticleRepository articleRepository;

    /**
     * Find comments by article UUID
     * 
     * @param articleUuid UUID of the article
     * @return List of Comment entities
     */
    public List<Comment> findCommentsByArticleUuid(UUID articleUuid) {
        Article article = articleRepository.findByUuid(articleUuid);
        if (article == null) {
            throw new NotFoundException();
        }

        return commentRepository.findAllByArticleUuidInOrderByCreatedAtDesc(articleUuid);
    }

    /**
     * Create a new comment for an article
     * 
     * @param articleUuid UUID of the article
     * @param userUuid    UUID of the user
     * @param content     Content of the comment
     */
    public void createComment(String articleUuid, String userUuid, String content) {
        Article article = articleRepository.findByUuid(UUID.fromString(articleUuid));
        if (article == null) {
            throw new NotFoundException();
        }

        Comment comment = Comment.builder()
                .article(article)
                .userUuid(UUID.fromString(userUuid))
                .content(content)
                .build();

        commentRepository.save(comment);
    }

}
