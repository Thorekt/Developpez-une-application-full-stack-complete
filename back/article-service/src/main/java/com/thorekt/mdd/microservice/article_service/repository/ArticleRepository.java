package com.thorekt.mdd.microservice.article_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.thorekt.mdd.microservice.article_service.model.Article;

/**
 * Repository for Article entity
 * 
 * @author thorekt
 */
@Repository
public interface ArticleRepository extends CrudRepository<Article, UUID> {

    /**
     * Find an article by UUID
     * 
     * @param uuid UUID of the article
     * @return Article entity
     */
    Article findByUuid(UUID uuid);

    /**
     * Find all articles ordered by creation date descending
     * 
     * @return List of Article entities
     */
    List<Article> findAllByOrderByCreatedAtDesc();

    /**
     * Find all articles ordered by creation date ascending
     * 
     * @return List of Article entities
     */
    List<Article> findAllByOrderByCreatedAtAsc();

    /**
     * Find all articles by theme UUID ordered by creation date descending
     * 
     * @param themeUuid UUID of the theme
     * @return List of Article entities
     */
    List<Article> findByThemeUuidOrderByCreatedAtDesc(UUID themeUuid);

    /**
     * Find all articles by theme UUID ordered by creation date ascending
     * 
     * @param themeUuid UUID of the theme
     * @return List of Article entities
     */
    List<Article> findByThemeUuidOrderByCreatedAtAsc(UUID themeUuid);

}
