package com.thorekt.mdd.microservice.article_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.thorekt.mdd.microservice.article_service.exception.NotFoundException;
import com.thorekt.mdd.microservice.article_service.model.Article;
import com.thorekt.mdd.microservice.article_service.repository.ArticleRepository;

import lombok.RequiredArgsConstructor;
import utils.OrderEnum;

/**
 * Service for managing articles.
 * 
 * @author Thorekt
 */
@Service
@RequiredArgsConstructor
public class ArticleService {
    /**
     * Article repository.
     */
    private final ArticleRepository articleRepository;

    /**
     * Find all articles ordered by creation date
     * 
     * @param themeUuids List of UUIDs of the themes
     * @param order      OrderEnum indicating ascending or descending order
     * @return List of Article entities
     */
    public List<Article> findAllArticlesByThemesUuidsInOrder(List<UUID> themeUuids, OrderEnum order) {
        if (order.isDesc()) {
            return articleRepository.findByThemeUuidInOrderByCreatedAtDesc(themeUuids);
        } else {
            return articleRepository.findByThemeUuidInOrderByCreatedAtAsc(themeUuids);
        }
    }

    /**
     * Create a new article
     * 
     * @param userUuid  UUID of the user
     * @param themeUuid UUID of the theme
     * @param title     Title of the article
     * @param content   Content of the article
     */
    public void createArticle(String userUuid, String themeUuid, String title, String content) {
        Article article = Article.builder()
                .userUuid(UUID.fromString(userUuid))
                .themeUuid(UUID.fromString(themeUuid))
                .title(title)
                .content(content)
                .build();

        articleRepository.save(article);
    }

    /**
     * Find an article by UUID
     * 
     * @param uuid UUID of the article
     * @return Article entity
     */
    public Article findArticleByUuid(UUID uuid) {
        Article article = articleRepository.findByUuid(uuid);
        if (article == null) {
            throw new NotFoundException();
        }
        return article;
    }
}
